<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="i"  begin=""  end="170"  step="1">
	<img src="/oa/images/avatar/${i}.jpg" style="width:48px"  onclick="window.parent.selectAvatar(${i});" />
</c:forEach>
<script type="text/javascript">
</script>
