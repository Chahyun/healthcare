# 개인프로젝트 - 헬스케어 어플리케이션
사용자의 (주, 일, 월) 운동 및 식단을 기록하고, 그 일정에 맞춰 시행할 수 있게 도움을 주는 어플리케이션입니다.

# 주요기능
- 운동 계획 등록 및 수정
  - 사용자는 주/일 별로 운동 계획을 등록하고 수정할 수 있습니다. 각 운동의 종목, 무게 등을 설정하여 개인에게 맞는 운동 계획을 수립할 수 있다.
  - 각 세트마다 운동의 무게가 다를 수 있기 때문에 따로 db에 저장해야 함.

- 운동 정보 조회
  -  나의 운동 정보 조회 월/주/일 마다의 운동 기록 및 예정 정보를 확인할 수 있다.
    - 각 운동마다 운동완료, 운동에정, 미시행이 있다.
      - 계획 등록 시 -> 운동에정
      - 사용자가 운동완료 체크시 - > 운동 완료로 변환
      - 사용자가 그 날까지 운동완료로 바꾸지 않을시 -> 미시행

- 나의 식단 정보 등록 및 수정
  - 사용자는 주/일 별로 식단 계획을 등록하고 수정할 수 있습니다. 각 식단의 칼로리 및 영양 정보를 등록할 수 있다.

- 다른 유저들의 운동 정보 조회
  - 유저들의 오늘의 운동 정보 리스트를 불러옵니다.(유저 정보가 비공개/정지 인 계정은 보이지 않음)

- 다른 유저들의 식단 정보 조회
  - 유저들의 오늘의 식단 정보 리스트를 불러옵니다.(유저 정보가 비공개/정지 인 계정은 보이지 않음)
 
- 회원가입
  - 유저들의 큰 개인정보가 필요하지 않는 어플리케이션이므로 아이디, 비밀번호, 닉네임만 설정하게 한다.
  - 비밀번호는 인코딩을 하여 변경된 값으로 저장한다.
  - 회원가입시 유저의 상태는 공개상태이다.

- 로그인
  - 로그인시 입력받은 아이디와 비밀번호를 이용하여, 아이디에 맞는 비밀번호를 가져와 인코딩한 값과 일치하면 jwt토큰을 발급하며 로그인이 된다.

- 나의 정보 수정
   - 나의 정보 비밀번호와 공개/비공개 여부를 변경할 수 있다.
   - 키 몸무게 근육량등 여러 정보를 업데이트 시킬 수 있다.
   - 비밀번호와 닉네임을 변경할 수 있다.

- 게시판
  - 게시판에 글 및 사진을 올릴 수 있어야 한다.
  - 게시판글에 댓글을 작성할 수 있어야 한다.
  - 게시판글에 좋아요를 누룰 수 있다.
  - 게시판글에 조회수도 나타난다.

- 유저간의 채팅
  - 센드버드 api를 사용하여 유저간의 채팅을 할 수 있다.

# ERD
![image](https://github.com/Chahyun/healthcare/assets/48889083/3e02fac6-b530-4511-b51e-0578d3705056)

# 기술스택
<div align=center> 
 <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
 <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
 <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
 <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 
 <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/fontawesome-339AF0?style=for-the-badge&logo=fontawesome&logoColor=white">
</div>
 
 
 
  

 
 
