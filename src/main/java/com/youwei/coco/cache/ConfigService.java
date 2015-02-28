package com.youwei.coco.cache;

import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

@Module(name="/config/")
public class ConfigService {

	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		for(Object key : ConfigCache.keySet()){
			mv.data.put(key, ConfigCache.get(key.toString() , ""));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView reload(){
		ModelAndView mv = new ModelAndView();
		ConfigCache.reload();
		mv.returnText = "配置文件重新加载成功";
		return mv;
	}
}
