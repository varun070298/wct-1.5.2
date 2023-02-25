<%	
    byte[] bytesBuffer = (byte[]) request.getAttribute("bytesBuffer");
    String contentType = (String) request.getAttribute("contentType");
	response.setHeader("Content-Type", contentType);    
    response.getOutputStream().write(bytesBuffer);
  
%>