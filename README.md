### 개인별 도서 정기 배송 서비스
> 프로젝트 기간 : 026.01.13 ~ 2026.04  
> 프로젝트 개요 : 월/년 단위 구독권을 활용한 구독 사용자 개인별 도서 정기 배송 시스템 개발

## 사용 기술 (Tech Stack)

| 분야 | 스택 |
| :---: | :--- |
| **Backend** | ![Java](https://img.shields.io/badge/Java_17-007396?style=flat-square&logo=OpenID_Connect&logoColor=white) ![SpringBoot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=Spring_Boot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL&logoColor=white) ![SpringDataJPA](https://img.shields.io/badge/Spring_Data_JPA-59666C?style=flat-square) ![SpringSecurity](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=Spring_Security&logoColor=white) ![QueryDSL                 ](https://img.shields.io/badge/QueryDSL-0778B9?style=flat-square) |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL&logoColor=white) |
| **CI/CD** | ![AWS](https://img.shields.io/badge/AWS_EC2-FF9900?style=flat-square&logo=Amazon_EC2&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white) ![GitHubActions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=GitHub_Actions&logoColor=white) |
| **Etc** | ![Flyway](https://img.shields.io/badge/Flyway-D65A31?style=flat-square&logo=Flyway&logoColor=white) ![Mockito](https://img.shields.io/badge/Mockito-52616B?style=flat-square) ![JMeter](https://img.shields.io/badge/JMeter-A32B2B?style=flat-square&logo=Apache_JMeter&logoColor=white) |

## ⌨️ ERD

<img width="992" height="736" alt="image" src="https://github.com/user-attachments/assets/c4439e71-a455-4e5f-8365-95d26cbf6bb4" />

[소개]

구독권을 통해 월마다 도서를 정기적으로 배송해주는 웹사이트

[기간]

2026.01.13~2026.04.16 (3개월)

[인원]

1명 (백엔드 1명)

[주요 개발 기능]

- Spring Security 및 jWT를 활용한 인증•인가 기능 구현
- 도서 API를 활용한 REST API 기능 개발
- 토스 페이먼츠를 활용한 결제 시스템 구현
- 카카오 API를 활용한 배송지 관리 시스템 구축
- 보조적으로 Gemini를 활용한 프론트 화면 구현

**화면 및 기능 소개**

[로그인 및 회원가입]  
<img width="353" height="357" alt="image" src="https://github.com/user-attachments/assets/0fed9880-27c6-405b-bdc7-9928df6736ea" />

<img width="403" height="790" alt="image" src="https://github.com/user-attachments/assets/be8f9a6a-6974-491b-8734-81f85380ed16" />
  
[메인페이지]    
<img width="1463" height="925" alt="image" src="https://github.com/user-attachments/assets/74adb7f1-ec87-45df-9354-92950031fa75" />

  
[도서 검색 페이지]         
<img width="1473" height="703" alt="image" src="https://github.com/user-attachments/assets/b51064c3-f95d-4ef4-97d1-4979c237b39a" />

  
[마이페이지]  

- DB에 저장된 인기 도서 100권 중 사용자가 읽은 도서를 제외한 4권 정기 배송 도서로 선정
- 결제일 기준으로 결제 예정일 표시
- 도서 구독권 가입/해지 기능

<img width="1451" height="699" alt="image" src="https://github.com/user-attachments/assets/77279000-da6b-4e1e-9ce8-e33049cb4a4f" />

  
[결제 화면]  
<img width="860" height="316" alt="image" src="https://github.com/user-attachments/assets/6e649730-cbc7-4b5f-834c-cdee27afc7b4" />

<img width="930" height="779" alt="image" src="https://github.com/user-attachments/assets/b50f7780-d6a8-4c37-9858-5ca9a696a81e" />
  

- 토스페이먼츠를 이용한 결제 시스템 구현
