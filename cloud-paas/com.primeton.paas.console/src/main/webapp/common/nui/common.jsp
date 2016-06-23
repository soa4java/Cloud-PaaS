<%
	String contextPath=request.getContextPath();
%>

<script type="text/javascript" src="<%=contextPath%>/common/nui/nui.js"></script>
<script>
	$(function(){
		nui.context='<%=contextPath %>'
	})
</script>
