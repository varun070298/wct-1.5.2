<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	// Expires immediately.
	response.addHeader("Expires", "-1");
	
	// always modified 
	// Need to convert from PHP.
	//header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); 
	
	// HTTP/1.1
	response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");

	// HTTP/1.0
	response.addHeader("Pragma", "no-cache");
%>
<%-- Layout Tiles 
  This layout create a html page with <header> and <body> tags. It render
   a header, left menu, body and footer tile.
  @param title String use in page title
  @param header Header tile (jsp url or definition name)
  @param menu Menu 
  @param body Body
  @param footer Footer
--%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">    
<HTML>
  <HEAD>
  	<base href="<%=basePath%>">   
    <link rel="stylesheet" media="screen" href="styles/styles.css" type="text/css" title="WCT" />
    <link rel="stylesheet" media="screen" href="styles/basic.css" type="text/css" title="WCT" />
    <style media="print">
      .noPrint { display: none; }
    </style>
    <title><tiles:getAsString name="title"/></title>
	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">	
  </HEAD>
<body> 
<a name="top"></a>
<div class="noPrint">
<tiles:insert attribute="header">
  <tiles:put name="page-help">
    <tiles:getAsString name="page-help"/> 
  </tiles:put>
</tiles:insert>
<tiles:insert page="top-nav-bar.jsp">
  <tiles:put name="activeTab"><tiles:getAsString name="activeTab"/></tiles:put>
  <tiles:put name="pageIcon"><tiles:getAsString name="pageIcon"/></tiles:put>
</tiles:insert>
</div>
<div id="pageBody">
	<div id="pageContent">
		<div class="noPrint">		
		<tiles:insert attribute="page-title" />
		<tiles:insert attribute="validation" />	
		</div>		
		<tiles:insert attribute="body" />
	</div>
</div>
<script language="javascript" src="scripts/_javascript.jsp"></script>
</body>
</html>