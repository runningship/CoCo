package com.youwei.coco.im;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.youwei.coco.CocoService;
import com.youwei.coco.KeyConstants;
import com.youwei.coco.cache.ConfigCache;
import com.youwei.coco.im.entity.GroupMessage;
import com.youwei.coco.im.entity.Message;
import com.youwei.coco.im.entity.UserGroupStatus;
import com.youwei.coco.user.entity.User;
import com.youwei.coco.util.DataHelper;
import com.youwei.coco.util.URLUtil;

public class IMServer extends WebSocketServer{

	private static IMServer instance =null;
	static Map<Integer,WebSocket> conns = new HashMap<Integer,WebSocket>();
//	static Map<String,Map<Integer,WebSocket>> conns = new HashMap<String,Map<Integer,WebSocket>>();
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CocoService cocoService = TransactionalServiceHelper.getTransactionalService(CocoService.class);
	private IMServer() throws UnknownHostException {
//		super(new InetSocketAddress(Inet4Address.getByName("localhost"), 9099));
//		super(new InetSocketAddress("192.168.1.125", 9099));
		super(new InetSocketAddress(ConfigCache.get("domainName" , "www.zhongjiebao.com"), 9099));
	}

	public static void startUp() throws Throwable{
		if(instance!=null){
			instance.stop();
			instance.finalize();
		}
		instance = new IMServer();
		instance.start();
		LogUtil.info("IM server started on port 9099");
	}
	
	public static void forceStop() throws IOException, InterruptedException{
		if(instance!=null){
			instance.stop();
		}
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		//希望在这个地方就实现用户信息的确认
		String path = handshake.getResourceDescriptor();
		path = path.replace("/?", "");
		System.out.println("web socket connector path is: "+path);
		try {
			Map<String, Object> map = URLUtil.parseQuery(path);
			Integer uid = Integer.valueOf(map.get("uid").toString());
			User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, uid);
			conn.getAttributes().put("uid", uid);
			conn.getAttributes().put("uname", user.uname);
			conns.put(uid, conn);
			nofityStatus(conn, KeyConstants.User_Status_Online);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Integer uid = (Integer) conn.getAttributes().get("uid");
		WebSocket removed = conns.remove(uid);
		if(removed!=null){
			conns.remove(removed);
			nofityStatus(conn, KeyConstants.User_Status_Offline);
		}
		System.out.println(conn+" removed ");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		LogUtil.info(message);
		if("ping".equals(message)){
			return;
		}
		JSONObject data = JSONObject.fromObject(message);
		Integer senderId = Integer.valueOf(conn.getAttributes().get("uid").toString());
		if("msg".equals(data.getString("type"))){
			int recvId = data.getInt("contactId");
			//接收人自己的信息没有必要发送，以免混淆
			data.remove("contactId");
			data.remove("contactName");
			data.remove("avatar");
			sendMsg(conn ,senderId,recvId,data , false);
		}else if("groupmsg".equals(data.getString("type"))){
			onReceiveGroupMsg(data,senderId);
		}
	}

	private void onReceiveGroupMsg(JSONObject data , int senderId){

		Integer groupId = data.getInt("contactId");
		//get users of group
		List<Map> list = SimpDaoTool.getGlobalCommonDaoService().listAsMap("select id as uid from User where cid=? or did=?", groupId , groupId);
		//save group message
		GroupMessage gMsg = new GroupMessage();
		gMsg.conts = data.getString("msg");
		gMsg.senderId = senderId;
		gMsg.groupId = groupId;
		gMsg.sendtime = new Date();
		dao.saveOrUpdate(gMsg);
		UserGroupStatus ugs = dao.getUniqueByParams(UserGroupStatus.class, new String[]{"groupId" , "receiverId"}, new Object[]{groupId , senderId});
		if(ugs==null){
			ugs = new UserGroupStatus();
			ugs.groupId = groupId;
			ugs.receiverId = senderId;
			ugs.lasttime = new Date();
		}else{
			ugs.lasttime = new Date();
		}
		dao.saveOrUpdate(ugs);
		for(Map map : list){
			Integer recvId = Integer.valueOf(String.valueOf(map.get("uid")));
			if(recvId.equals(senderId)){
				continue;
			}
			WebSocket conn = conns.get(recvId);
			if(conn!=null){
				//send group message
				data.put("sendtime", DataHelper.sdf4.format(new Date()));
				data.put("senderId", senderId);
				conn.send(data.toString());
			}
		}
	
	}
	
	private void nofityStatus(WebSocket from , int status) {
		Object fromUid = from.getAttributes().get("uid");
		Object fromUname = from.getAttributes().get("uname");
		
		JSONObject jobj = new JSONObject();
		jobj.put("type", "user_status");
		jobj.put("status", status);
		jobj.put("contactId", fromUid);
		jobj.put("contactName", fromUname);
		for(Map buddy : cocoService.getBuddyList(null)){
			WebSocket conn = conns.get(buddy.get("id"));
			if(conn!=null){
				conn.send(jobj.toString());
			}
		}
	}

	private void sendMsg(WebSocket senderSocket ,Integer senderId , Integer recvId , JSONObject data , boolean isGroup) {
		if(!isGroup){
			Message dbMsg = new Message();
			dbMsg.sendtime = new Date();
			dbMsg.conts = data.getString("msg");
			dbMsg.senderId = senderId;
			dbMsg.receiverId = recvId;
			dbMsg.hasRead=0;
			try{
				dao.saveOrUpdate(dbMsg);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		data.put("sendtime", DataHelper.sdf4.format(new Date()));
		WebSocket recv = conns.get(recvId);
		data.put("senderId", senderId);
		if(recv!=null){
			recv.send(data.toString());
		}
	}

	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}
	
	public static boolean isUserOnline(int userId){
		return conns.containsKey(userId);
	}
	
	/**
	 * 推送消息到群组
	 * @param groupId
	 * @param msg
	 */
	public static void pushMsgToGroup(int groupId, String msg){
		//{"contactId":"1","type":"groupmsg","contactName":"中介宝","currentPageNo":"1","msg":"<p>srui<\/p>","senderAvatar":157,"senderName":"孟浩"}
		JSONObject data = new JSONObject();
		data.put("msg", msg);
		data.put("type", "groupmsg");
		data.put("contactId", groupId);
		data.put("senderAvatar", CocoService.AssistantAvatar);
		data.put("senderName", CocoService.AssistantName);
		instance.onReceiveGroupMsg(data,CocoService.AssistantUid);
	}

	/**
	 * 推送消息给个人
	 * @param userId
	 * @param msg
	 */
	public static void pushMsgToUser(int userId,String msg){
		System.out.println(msg);
		WebSocket conn = conns.get(userId);
		if(conn==null){
			return;
		}
		JSONObject jobj = new JSONObject();
		jobj.put("senderId", CocoService.AssistantUid);
		jobj.put("sendtime", DataHelper.sdf4.format(new Date()));
		jobj.put("type", "msg");
		jobj.put("msg", msg);
		jobj.put("senderAvatar", CocoService.AssistantAvatar);
		jobj.put("senderName", CocoService.AssistantName);
		conn.send(jobj.toString());
		
		Message dbMsg = new Message();
		dbMsg.sendtime = new Date();
		dbMsg.conts = msg;
		dbMsg.senderId = CocoService.AssistantUid;
		dbMsg.receiverId = userId;
		dbMsg.hasRead=0;
		instance.dao.saveOrUpdate(dbMsg);
	}
}
