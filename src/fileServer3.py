from socket import *
import datetime
import signal
import time
import sys
import os
	
def validRequest(request):
	headers = request.split("\r\n")
	getRequest = headers[0].split(' ')
	host = 0
	connection = 0
	userAgent = 0
	acceptLanguage = 0
	
	if len(getRequest) != 3 or getRequest[0] != "GET":		return False
	if len(headers) < 6:
		return False
	for x in headers:
		y = x.split(' ')
		if y[0] == "Host:":
			host = 1
		if y[0] == "Connection:":
			connection = 1
		if y[0] == "User-Agent:":
			userAgent = 1
		if y[0] == "Accept-Language:":
			acceptLanguage = 1
			
	return host and connection and userAgent and acceptLanguage
	
def Main():
	serverName = "Mayo/1.8.87 (MAYO)"
	
	try:
		port = int(sys.argv[1])
	except:
		sys.exit(0)
	
	print("Port: " + str(port))
	
	serverSocket = socket(AF_INET, SOCK_STREAM)
	#serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
	serverSocket.bind(('', port))
	serverSocket.listen(8)
	#signal.signal(signal.SIGINT, signal_handler(signal, serverSocket))

	#s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	#s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	#s.bind((HOST, PORT))


	print("Server Started.")

	while True:
		sock, addr = serverSocket.accept()
		httpRequest = sock.recv(8192).decode()
		headers = httpRequest.split("\r\n")
		getRequest = headers[0].split(' ')
		tnow = time.localtime()
		tnowstr = time.strftime('%a, %d %b %Y %H:%M:%S %Z', tnow)
		type = [".html", ".htm", ".txt", ".jpg", ".jpeg"]
		contentTypes = {".html": "text/HTML", ".htm": "text/HTM", ".txt": "text/plain", ".jpg": "image/JPG", ".jpeg": "image/JPEG"}
		noMod = 1
		done = 1
		noFile = 1
		
		for x in headers:
			print("X: " + x)
		

		response = ""
		fileType = ""
		filename = ""
		header = ""
		date = ("Date: " + tnowstr + "\r\n")
		server = ("Server: " + serverName + "\r\n")
		last = ""
		accept = ""
		contentLen = ""
		contentType = ""
		br = ("\r\n")
		content = ""

		#print("ZZZ: " + getRequest[2])
		#print(getRequest[2] != "HTTP/1.1")

		if not validRequest(httpRequest):
			#print("HERE!")
			response = ("400 Bad Request")
			noFile = 0
		elif getRequest[2] != "HTTP/1.1":
			#print("Funny")
			response = ("505 HTTP Version Not Supported")
			noFile = 0
		elif not any(x in getRequest[1] for x in type):
			#print("bounce")
			response = ("415 Unsupported type")
			noFile = 0
	
		if getRequest[0] == "GET":
			filename = getRequest[1][1:]
			
		#print("Nofile" + str(noFile))
			
		if os.path.isfile(filename) and noFile:
			ftime = os.path.getmtime(filename)
			ftime = time.strftime('%a, %d %b %Y %H:%M:%S %Z', time.localtime(ftime))
			length = os.path.getsize(filename)
			
			for x in headers:
				if ("If-Modified-Since: " + ftime) == x:
					response = ("304 Not Modified")
					noMod = 0

			for x in type:
				if x in filename:
					fileType = contentTypes[x]
					break
					
			if noMod:
				response = ("200 OK")
				last = ("Last-Modified: " + ftime + "\r\n")
				accept = ("Accept-Ranges: bytes\r\n")
				contentLen = ("Content-Length: " + str(length) + "\r\n")
				contentType = ("Content-Type: " + fileType + "\r\n")
			
				header = ("HTTP/1.1 " + response + "\r\n")
				sock.send((header + date + server + last + accept + contentLen + contentType + br).encode())
			
				with open(filename, 'rb') as f:
					bytesToSend = f.read(8192)
					sock.send(bytesToSend)
					
					while bytesToSend:
						bytesToSend = f.read(8192)
						sock.send(bytesToSend)
						
				sock.close()
				done = 0
		elif noFile:
			response = ("404 Not Found")
		
		if done:
			header = ("HTTP/1.1 " + response + "\r\n")
			content = ("<html><body> " + response + "</body></html>")
			sock.send((header + date + server + last + accept + contentLen + contentType + br + content).encode())
			
		sock.close()
	serverSocket.close()
	
'''def signal_handler(signal, sock):		
	print('You pressed Ctrl+C!')
	#print("Z: " + str(signum))
	print("Jeezzy")
	#if ()
	sock.close()
	sys.exit(0)'''

if __name__ == '__main__':
	Main()