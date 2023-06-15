# DORUNDORUN

<img src="https://user-images.githubusercontent.com/72375179/229091790-eac4b500-8ec8-4bbd-a1bc-22eca9d7b56e.png" style="width:100%;">

## ✨프로젝트 소개

### 🔍 주요 기능
- `화상 채팅` 😎 웹캠을 통해 상대방의 얼굴을 보며 🎤 보이스 채팅을 할 수 있습니다. 
- `실시간 채팅` ✏️ 상대방과 실시간 채팅이 가능합니다.
- `드로잉` 🎨 화이트 보드에 그림을 그려보거나 상대방의 얼굴에 낙서를 해볼 수 있습니다.
- `캡처` 📸 자기가 그린 그림을 캡처해서 채팅방에 전송할 수 있습니다.
- `프로젝트 주소` 🧑‍💻 <a href="https://dorun-dorun.vercel.app/">두런두런 둘러보기</a>

## 👀 서비스 대표 이미지

### 1. 방목록 ~ 검색
<img src="https://user-images.githubusercontent.com/72375179/225041698-e5511bdf-760e-44f0-bec1-589661d98673.gif">

### 2. 방생성 ~ 입장
<img src="https://user-images.githubusercontent.com/72375179/225041817-b9a92669-bbb5-4ca5-9e3c-f93b52639d2e.gif">

### 3. 방초대 ~ 참여자 ~ 입장
<img src="https://user-images.githubusercontent.com/72375179/225042037-a58e92a7-f434-4ef3-b82f-64bdeb0c31f4.gif">

### 4. 채팅방 기능
<img src="https://user-images.githubusercontent.com/72375179/225044157-c1da89e4-193c-45ca-9ac2-18c05703084a.gif">

## 🛠 아키텍쳐
<img src="https://user-images.githubusercontent.com/72375179/224369810-e86526cd-5451-4b38-954e-0c818e41b3d8.png" style="width:1000px; height:500px;">

### 🤔 트러블 슈팅
<details>
<summary>FRONT-END</summary>
<div markdown="1">
  <img src="https://user-images.githubusercontent.com/72375179/225014701-bc91f98e-82ec-410f-8f26-fffc887fa4c2.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225014867-d3ca160e-6ef8-4a00-853d-af6daa656cd3.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225014963-5bd5fc05-caaa-44c9-8d2e-ca16707c8465.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225015070-c36a4d01-3ee2-4609-8bf9-f0fec981b782.png"/>
</div>
</details>

<details>
<summary>BACK-END</summary>
<div markdown="1">
  <img src="https://user-images.githubusercontent.com/72375179/224990114-197afb2b-fa33-4ffb-b2cb-8ba4c9cad1c2.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/224991223-a46159a9-472c-49fd-bced-90098f4f6c1d.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/224991324-d1c1a00b-c10f-446a-b27e-83500b89ed87.png"/>
</div>
</details>

### 🎯 기술적 도전
<details>
<summary>FRONT-END</summary>
<div markdown="1">
  <img src="https://user-images.githubusercontent.com/72375179/225019515-062633ee-942e-48af-9d96-95a8f0d97460.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225019645-bbad88ea-611f-411c-a3ab-9a3c6208dd00.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225019723-ef087f9d-22a9-4436-928b-7e24c8adbf40.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/225019811-e1b57c29-48ee-4b58-9324-942749b0f8ab.png"/>
</div>
</details>

<details>
<summary>BACK-END</summary>
<div markdown="1">
  <img src="https://user-images.githubusercontent.com/72375179/224991515-d0679ee6-73e0-44c5-b454-56e538d55868.png"/>
  <img src="https://user-images.githubusercontent.com/72375179/224991648-7e37f10a-7c77-4063-8fd4-f9f9349a9280.png"/>
</div>
</details>

### 💡기술적 의사 결정
<details>
<summary>FRONT-END</summary>
<div markdown="1">
  <table>
    <tr>
      <td><b>사용 기술<b></td>
      <td><b>의사 결정<b></td>
      <td><b>결과<b></td>
    </tr>
    <tr>
      <td>canvas</td>
      <td>드로잉 기능을 구현하기 위해 canvas 또는 webGL을 선택할 수 있었고,
canvas가 webGL에 비해 러닝 커브가 낮고, 비교적 간단하게 구현할 수 있다는 장점과 우리 서비스에서는 webGL의 3D기능은 불필요하므로 canvas를 선택함</td>
      <td>canvas의 간단한 사용 방법 덕분에 짧은 기간내에 서비스에서 목표로 하는 드로잉 기능 구현을 구현함</td>
    </tr>
    <tr>
      <td>zustand</td>
      <td>리액트 상태관리를 위해 보편적으로 많이 사용되는 redux의 보일러플레이트 이슈로 인해 더 간결한 방식인 zustand를 도입함</td>
      <td>redux의 보일러플레이트 이슈 없이 간결한 코드로 상태관리가 가능해져 개발과 유지보수 등 작업 효율성이 증가함</td>
    </tr>
    <tr>
      <td>axios</td>
      <td>비동기로 HTTP통신을 하기 위해 브라우저 호환성이 높은 AXIOS를 선택함</td>
      <td>크롬, 파이어폭스, 엣지 등 다양한 브라우저에서 서버통신 기능들이 정상 작동하고, Promise 기반으로 만들어졌기 때문에 원하는 방식으로 데이터 처리 작업이 가능해짐</td>
    </tr>
    <tr>
      <td>styled-components</td>
      <td>모듈성을 위한 css 모델을 문서레벨이 아닌 컴포넌트 레벨로 추상화 한다는 점과 js와 css 사이의 상수와 함수를 쉽게 공유할 수 있고 props를 활용한 조건부 스타일링이 가능하다는 장점이 이번 서비스에 적합하다고 판단하여 도입함</td>
      <td>모듈화와 조건부 스타일링 덕분에 컴포넌트 재활용성이 증가하였고 하나의 컴포넌트로 다양한 처리가 가능해짐</td>
    </tr>
    <tr>
      <td>React-router-dom</td>
      <td>component 구조 덕분에 부분적 변화를 효율적으로 반영 할 수 있다는 리액트의 장점을 극대화 할 수 있다는 점에서 도입함</td>
      <td>컴포넌트 업데이트를 통하여 새로고침을 하지 않고 페이지 이동이 가능해져 유저 입장에서 쾌적한 서비스 이용이 가능해짐</td>
    </tr>
    <tr>
      <td>Intersection-observer</td>
      <td>브라우저 뷰포트(Viewport)와 설정한 요소(Element)의 교차점을 관찰하며, 사용자의 현재 화면에 타겟 요소가 보이는지를 판단하는 것을 기준으로 비동기로 작동하기 때문에 scroll 같은 이벤트 기반의 요소 관찰에서 발생하는 렌더링 성능이나 이벤트 연속 호출 같은 문제 없이 사용 가능하다는 점에서 도입함</td>
      <td>성능적으로 효율적이고 비동기적으로 작동하는 라이브러리의 특성상 랜더링 이슈 없이 무한 스크롤링이 가능해짐</td>
    </tr>
  </table>
</div>
</details>

<details>
<summary>BACK-END</summary>
<div markdown="1">
  <table>
    <tr>
      <td><b>사용 기술<b></td>
      <td><b>의사 결정<b></td>
      <td><b>결과<b></td>
    </tr>
    <tr>
      <td>Openvidu</td>
      <td>프로젝트 초기, Openvidu를 사용하지 않고, WebRTC로만 구현을 시도했지만, 구현 난이도, 플랫폼 간 문제, 안정성 저하 등의 문제가 발생하여 이러한 문제들을 해결하고자Openvidu 사용.</td>
      <td>개발 속도가 올라감, 안정성이 향상됨, 프로젝트 초기 백엔드의 기본 코드 만으로도 프런트엔드에서 다대다 화상,음성 관련 코드 작성 및 테스트가 가능.</td>
    </tr>
    <tr>
      <td>Redis (NoSql)</td>
      <td>1. 애플리케이션이 실행되는 단일 서버에서만 채팅 메시지가 처리되는 문제가 있었음. 이를 해결하기 위해 Redis의 pub/sub을 사용하자는 의견이 나옴
        2. 기존에 RDB로 Refresh Token을 관리해 왔지만, 모든 요청에서 토큰을 검사하고 Database에서 확인하므로, 오버헤드가 클 것으로 판단 + 리프레쉬 토큰의 유효기간이 지나면 스케줄러를 사용해야 하는 불편함이 있다.</td>
      <td>1. Redis의 pub/sub을 사용해서 다수의 서버가 채팅 메시지를 교환 할 수 있는 환경을 구축함.
        <br>
2-1. Redis 의 TimeToLive를 사용하여 삭제기간을 설정함<br>
2-2. 많은 요청들을 Redis로 연결하여 오버헤드를 줄임.</td>
    </tr>
    <tr>
      <td>Refresh Token</td>
      <td>기존 Access Token 하나 만을 사용 시, 서비스 사용자는 자주 로그인을 해야하므로 불편함을 겪고, 그렇다고 Access Token 만료기간을 늘리면 토큰 탈취 시 공격자가 오랜 시간 해당 계정을 사용할 수 있다는 단점이 있다.
그렇기 때문에, Refresh Token을 도입했고, Refresh Token Rotation(1회성 리프레쉬 토큰)을 사용해서, 엑세스 토큰이 만료 시 리프레쉬 토큰이 유효(기간도 유효)할 경우 엑세스, 리프레쉬 토큰 둘 다 재발급하여 Refresh Token이 2번 이상 사용 된다면 탈취됐다는 것을 확인할 수 있다. 또한 Database에서 Refersh Token을 삭제하면 해당 유저의 접근을 차단할 수 있고, 이러한 로직들로 인해 Database 오버헤드가 많이 생길 것으로 예상, Redis를 사용하였다.</td>
      <td>1. Access Token이 만료되더라도, Refresh Token으로 재발급 받으면서 로그인을 하여 사용자의 편리함 증가.<br>
        2. Refresh Token으로 재발급 할때 AT, RT 둘 다 재발급 받으므로, Refresh Token은 1회성으로만 사용할 수 있고 2번 이상 사용된다면 탈취 됐다는 것을 가정할 수 있음.<br>
3. 또한 RT로 AT, RT 재발급 시 RT의 만료기간 자체도 계속 길어지므로 사용자의 자동 로그인 기간이 더 늘어난다. → 토큰이 계속 바뀌므로 보안성을 높임</td>
    </tr>
    <tr>
      <td>logback</td>
      <td>서버를 배포하고 나면 실시간으로 로그를 확인하지 못해 어디서 오류가 생겼는지 판단하기 어려웠음. 로그를 실시간으로 확인하기 위해서 logback을 사용해서 AWS CloudWatch에 저장을 하기로 결정함.</td>
      <td>1. logback을 사용해서 CloudWatch에 로그를 저장 가능함<br>
2. CloudWatch를 통해 로그를 실시간으로 확인할 수 있게 되었음</td>
    </tr>
    <tr>
      <td>Grafana</td>
      <td>서버의 CPU 사용량이 높아 다운이 되었던 문제가 생겼었고, 이를 통해 모니터링의 필요성을 느끼게됨. 프로메테우스를 통해 메트릭 정보를 수집하고 그라파나를 이용해서 모니터링을 하자고 결정함</td>
      <td>1. 트래픽, 메모리, CPU 사용량을 실시간으로 모니터링 할 수 있게 되었음.<br>
2. 알림 기능을 구현하여 메모리, CPU 사용량이 높을때 Slack으로 메시지가 갈 수 있도록 구현함.</td>
    </tr>
  </table>
</div>
</details>

### 🔧 유저 피드백
<details>
<summary>유저 피드백</summary>
<div markdown="1">
  <img src="https://user-images.githubusercontent.com/72375179/224991893-b6ab24a2-09cb-44f9-8999-2d7a52628e1e.png"/>
</div>
</details>



### 🏅 기술 스택

**FRONT-END**
<br></br>
<img src="https://img.shields.io/badge/REACT-00D8FF?style=flat-square&logo=REACT&logoColor=white"/>
<img src="https://img.shields.io/badge/AXIOS-5A29E4?style=flat-square&logo=Axios&logoColor=white"/>
<img src="https://img.shields.io/badge/JAVASCRIPT-F7DF1E?style=flat-square&logo=JAVASCRIPT&logoColor=white"/>
<img src="https://img.shields.io/badge/VERCEL-000000?style=flat-square&logo=VERCEL&logoColor=white"/>
<img src="https://img.shields.io/badge/REACT ROUTER-CC3D3D?style=flat-square&logo=REACT ROUTER&logoColor=white"/>
<img src="https://img.shields.io/badge/STYLED-COMPONENTS-DB7093?style=flat-square&logo=STYLED-COMPONENTS&logoColor=white"/>
<img src="https://img.shields.io/badge/ZUSTAND-1DDB16?style=flat-square&logo=ZUSTAND&logoColor=white"/>
<img src="https://img.shields.io/badge/WEBRTC-000000?style=flat-square&logo=webrtc&logoColor=white"/>
<img src="https://img.shields.io/badge/OPENVIDU-1DDB16?style=flat&logo=OPENVIDU&logoColor=white"/>
<img src="https://img.shields.io/badge/MUI-007FFF?style=flat-square&logo=MUI&logoColor=white"/>
<img src="https://img.shields.io/badge/REACT-COLORPALETTE-FF4154?style=flat-square&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/REACT QUERY-FF4154?style=flat-square&logo=REACT QUERY&logoColor=white"/>
<img src="https://img.shields.io/badge/HTML5-CANVAS-F68D2E?style=flat-square&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/INTERSECTION OBSERVER-3B7D1C?style=flat-square&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/SockJS-000000?style=flat-square&logo=SockJS&logoColor=white"/>
<img src="https://img.shields.io/badge/STOMP-000000?style=flat-square&logo=STOMP&logoColor=white"/>


**BACK-END**
<br></br>
<img src="https://img.shields.io/badge/SPRING BOOT-1DDB16?style=flat-square&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/SPRING SECURITY-1DDB16?style=flat-square&logo=springsecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/OAUTH2-000000?style=flat-square&logo=OAUTH2&logoColor=white"/>
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JWT&logoColor=white"/>
<img src="https://img.shields.io/badge/REDIS-FF0000?style=flat-square&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/MYSQL-4374D9?style=flat-square&logo=mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/WEBRTC-000000?style=flat-square&logo=webrtc&logoColor=white"/>
<img src="https://img.shields.io/badge/AMAZON RDS-4374D9?style=flat-square&logo=amazonrds&logoColor=white"/>
<img src="https://img.shields.io/badge/AMAZON S3-ABF200?style=flat-square&logo=amazons3&logoColor=white"/>
<img src="https://img.shields.io/badge/AMAZON EC2-FFBB00?style=flat-square&logo=AMAZON EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/DOCKER-2496ED?style=flat&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/GITHUB-000000?style=flat&logo=GITHUB&logoColor=white"/>
<img src="https://img.shields.io/badge/GITHUB ACTIONS-0054FF?style=flat&logo=GITHUB ACTIONS&logoColor=white"/>
<img src="https://img.shields.io/badge/NGINX-1DDB16?style=flat&logo=NGINX&logoColor=white"/>
<img src="https://img.shields.io/badge/KURENTO-000000?style=flat&logo=KURENTO&logoColor=white"/>
<img src="https://img.shields.io/badge/OPENVIDU-1DDB16?style=flat&logo=OPENVIDU&logoColor=white"/>
<img src="https://img.shields.io/badge/AMAZON ROUTER 53-FFBB00?style=flat-square&logo=AMAZON ROUTER 53&logoColor=white"/>
<img src="https://img.shields.io/badge/HTTPS-FF5E00?style=flat-square&logo=HTTPS&logoColor=white"/>
<img src="https://img.shields.io/badge/SockJS-000000?style=flat-square&logo=SockJS&logoColor=white"/>
<img src="https://img.shields.io/badge/STOMP-000000?style=flat-square&logo=STOMP&logoColor=white"/>

### 📆 프로젝트 기간
2023.02.02 ~ 2023.03.15 / 서비스 런칭 : 2023.03.08

## 🎓 프로젝트 멤버
| 🔰 박청우 | 김준철 | 🔰 황현준 | 박관우 | 우채윤 |
| ------ | --- | --- | --- | --- | 
| <img src="https://user-images.githubusercontent.com/72375179/225024604-0174075b-d579-4cd1-8c70-0dba359748e6.png" style="width:100px; height:150px;"> | <img src="https://user-images.githubusercontent.com/72375179/224364990-584c5d45-246a-4afc-93d2-2e00d483e1ec.png" style="width:100px; height:150px;"> | <img src="https://user-images.githubusercontent.com/72375179/224364427-e93e2f20-f86b-49ed-b84c-eba520664d3b.png" style="width:100px; height:150px;"> | <img src="https://user-images.githubusercontent.com/72375179/224364345-707629db-c838-49c5-9124-e609f44c0277.png" style="width:100px; height:150px;"> | <img src="https://user-images.githubusercontent.com/72375179/225024712-e4524932-846c-4ffa-914a-74fbdf5575b5.png" style="width:100px; height:150px;"> |
| FRONT-END | FRONT-END | BACK-END | BACK-END | DESIGN |
