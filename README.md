# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

# 1. index.html 응답
## 기능요구사항 
- 정적인 html 파일 응답
  - http://localhost:8080/index.html 로 접속했을 때 /src/main/resources/templates
  디렉토리의 index.html 파일을 읽어 클라이언트에 응답하기.
- HTTP Request 내용 추력
  - 서버로 들어오는 HTTP Request의 내용을 읽고 로거(log.debug)를 이용해 출력하기.
## 구현 정리
- 브라우저에서 서버로 들어오는 요청의 모든 데이터는 InputStream에 들어있다.
  - InputStream in = connection.getInputStream();
- 서버에서 브라우저로 데이터를 보낼 때는 OutputStream에 실어서 보낸다.
  - OutputStream out = connection.getOutputStream()
- InputStream을 바로 읽기는 어렵기 때문에 BufferedReader로 변환해서 Header를 한줄 씩 읽는다.
  - BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
- Header의 첫 라인에 요청URl이 포함되어 있다.
- Header 마지막에는 빈 문자열이 들어있다.
  - 이를 이용해서 br.readLine()이 ""일 때 까지 읽으면 헤더 정보를 전부 얻을 수 있다.

## 