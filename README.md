<p align="left">
  <img src="frontend/src/assets/images/HARURAMENSARI.png" alt="HARURAMENSARI" width="30" style="vertical-align: middle;"/>
  <span style="vertical-align: middle;">be15-4th-PigMa-HaruSari</span>
</p>
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:ffb6c1,50:87cefa,100:ffe066&height=220&section=header&text=HaruSari&fontSize=75&fontColor=ffffff&animation=twinkling" alt="header" width="100%"/>
<p align="center">
<img src="frontend/src/assets/images/HARURAMENSARI.png" alt="HARURAMENSARI" width="100"/>
</p>



<div align="center">

### 📅HaruSari

체계적인 일정 관리와 통계, 회고, 맞춤형 피드백을 통해<br> 사용자의 자기 주도적 성장과 목표 달성을 지원하는 올인원 플랫폼

</div>

<br>

---

## 📚 목차

1. [pigma🐷  팀 소개](#1-pigma--팀-소개)
2. [협업🤝 전략](#2-협업-전략)
3. [프로젝트 기획서📝](#3-프로젝트-기획서)
4. [아키텍쳐 구조도⚙️](#4-아키텍쳐-구조도)
5. [기술 스택🛠️](#5-기술-스택)
6. [요구사항 명세서📋](#6-요구사항-명세서)
7. [ERD🗂️](#7-erd)
8. [REST API📡](#8-rest-api)
9. [주요 기능🚀](#9-주요-기능)
10. [빌드 및 배포 문서📦](#10-빌드-및-배포-문서)
11. [Jenkins CI/CD 테스트 결과](#11-jenkins-ci-cd-테스트-결과)
12. [💌 팀원 회고](#12--팀원-회고)


<br>

---

## 1. pigma🐷  팀 소개

<br>

| <img width="120" src="https://github.com/Lee-gi-yeun.png" /> | <img width="120" src="https://github.com/z00m-1n.png" /> | <img width="120" src="https://github.com/jang9465.png" /> | <img width="120" src="https://github.com/memory-h.png" /> | <img width="120" src="https://github.com/jihye25.png" /> |
|:------------------------------------------------------------:| :-------------------------------------: | :-------------------------------------------: | :-------------------------------------: | :-------------------------------------: 
|            [이기연](https://github.com/Lee-gi-yeun)             | [이주미](https://github.com/z00m-1n) | [장건희](https://github.com/jang9465) | [장현영](https://github.com/memory-h) | [최지혜](https://github.com/jihye25) 


<br>

---

## 2. 협업🤝 전략

<img src="assets/images/readme/git.png" width="400"/>

✔️ 각자 작업 브랜치를 생성하고, 관련 이슈를 등록
<br>
✔️ 작업을 완료한 후 커밋과 푸시를 진행
<br>
✔️ PR을 올리면 최소 2명이 코드 리뷰와 승인
<br>
✔️ 2명 이상 승인 시에만 main 브랜치로 머지 가능

---

## 3. 프로젝트 기획서📝

### 🧩 프로젝트 배경

> "현대인의 삶은 빠르게 변화하고 있습니다.  
> 다양한 역할과 책임 속에서 **체계적인 시간 관리**와  
> **자기 주도적 목표 달성**의 중요성이 점점 커지고 있습니다.
>
> 하지만 일상 속에서 해야 할 일들을 효율적으로 관리하고,  
> 스스로의 성과를 돌아보며 성장의 기회를 찾는 것은 쉽지 않은 일입니다."

### ❓ 문제점
- 기존 일정 관리 앱들은
    - 단순한 일정 등록과 알림 기능에 한정되어
    - **지속적인 동기 부여**와 **목표 달성 지원** 기능이 부족합니다.

### 🎯 프로젝트의 목표
- 사용자가 할 일(일정) 관리뿐만 아니라
- **회고**, **통계**, **알림**, **피드백**을 통해
- 자기 관리 역량을 높이고
- **지속적으로 성장**할 수 있도록 지원하는
- **통합 플랫폼 구축**

### 🌟 주요 기능

|      기능       | 설명                                           | 기대 효과                           |
|:-------------:|:---------------------------------------------|:--------------------------------|
| 📅 일정 등록 및 관리 | 일정을 손쉽게 추가, 수정, 삭제하고 완료 시 체크 표시로 구분          | 일상 관리의 편의성 향상                   |
| 🤖 반복 일정 자동화  | 반복되는 일정을 자동으로 추가 및 관리                        | 업무 효율성 증대                       |
| 🗂️ 카테고리별 관리  | 일정을 카테고리별로 분류 및 관리<br>카테고리별 색상 지정 및 완료 처리 가능 | 체계적 자기 관리 및 시각적 구분, 완료된 카테고리 정리 |
|   📊 달성률 통계   | 일정 및 목표의 **일일·월간 달성률**을 시각화<br>진행 상황을 한눈에 파악 | 동기 부여 및 자기 점검                   |
|  💡 맞춤형 피드백   | 달성 현황과 회고를 바탕으로 맞춤 피드백 제공                    | 지속적 성장 지원                       |
|     📝 회고     | 일정과 연동된 회고 작성 및 오늘의 할 일별 메모 가능               | 하루 성과와 느낀 점 기록하여 자기 성찰에 도움.     |

---

## 4. 아키텍쳐 구조도⚙️
<div align="center">
<img src="assets/images/readme/architectural.png" alt="requirement_statement"/>
</div>
<br>

---

## 5. 기술 스택🛠️

<div align="center">

### Frontend
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"/>
<img src="https://img.shields.io/badge/CSS3-0082F0?style=flat-square&logo=CSS3&logoColor=white"/>
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white"/>
<img src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat-square&logo=Vue.js&logoColor=white"/>
<img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=Nginx&logoColor=white"/>
<img src="https://img.shields.io/badge/Prettier-F7B93E?style=flat-square&logo=Prettier&logoColor=white"/>

<br>
<br>

### Backend
<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JWT&logoColor=white"/>
<img src="https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logo=JPA&logoColor=white"/>
<img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white"/>
<img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariaDB&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-FF4438?style=flat-square&logo=Redis&logoColor=white"/>
<img src="https://img.shields.io/badge/Rabbitmq-FF6600?style=flat-square&logo=Rabbitmq&logoColor=white"/>
<img src="https://img.shields.io/badge/Junit5-25A162?style=flat-square&logo=Junit5&logoColor=white"/>
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"/>

<br>
<br>

### CI / CD
<img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat-square&logo=GitHubActions&logoColor=white"/>
<img src="https://img.shields.io/badge/Argo CD-FE4C61?style=flat-square&logo=Argo&logoColor=white"/>

<br>
<br>

### Infrastructure
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/kubernetes-326CE5?style=flat-square&logo=kubernetes&logoColor=white"/>

### Collaboration Tools
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>
<img src="https://img.shields.io/badge/Google Docs-4285F4?style=flat-square&logo=GoogleDocs&logoColor=white"/>
<img src="https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=Discord&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=Figma&logoColor=white"/>

</div>

<br>

---

## 6. 요구사항 명세서📄

<img src="assets/images/readme/requirement_statement1.png" alt="requirement_statement" />
<img src="assets/images/readme/requirement_statement2.png" alt="requirement_statement"/>

---

## 7. ERD🗂️

<img src="assets/images/readme/harusari ERD.png" alt="harusariERD"/>

<br>

---

## 8. REST API📡

<details>
<summary>Swagger</summary>
<img src="assets/images/readme/swagger/Swagger%20UI-01.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-02.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-03.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-04.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-05.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-06.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-07.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-08.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-09.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-10.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-11.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-12.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-13.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-14.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-15.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-16.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-17.png" alt="harusariUi"/>
<img src="assets/images/readme/swagger/Swagger%20UI-18.png" alt="harusariUi"/>
</details>

<br>

---

## 9. 주요 기능🚀

### ⭐ 회원

<details>
<summary>이메일/소셜 회원 가입</summary>
・ 비회원은 이메일, 비밀번호, 닉네임, 성별을 입력해 회원가입할 수 있다. <br>
・ 비회원은 Google 또는 Kakao 계정으로 회원가입을 하고 닉네임과 성별을 등록할 수 있다.
<img src="/assets/images/readme/gif/registration.gif"/>
</details>

<br>

<details>
<summary>로그인</summary>
・ 회원은 이메일과 비밀번호를 입력해 로그인할 수 있다.
<img src="/assets/images/readme/gif/login.gif"/>
</details>

<br>

<details>
<summary>로그아웃</summary>
・ 회원은 로그아웃 버튼 클릭으로 로그인 세션을 종료할 수 있다.
<img src="/assets/images/readme/gif/logout.gif"/>
</details>


<br>

<details>
<summary>회원탈퇴</summary>
・ 회원은 자신의 계정을 탈퇴할 수 있다.
<img src="/assets/images/readme/gif/withdrawal.gif"/>
</details>

<br>

<details>
<summary>개인 정보 조회/수정</summary>
・ 회원은 자신의 이메일, 닉네임, 성별, 개인정보 수집 동의 여부를 확인할 수 있다. <br>
・ 회원은 닉네임, 개인정보 수집 동의 여부를 수정할 수 있다.
<img src="/assets/images/readme/gif/mypage.gif"/>
</details>

<br>

<details>
<summary>비밀번호 변경</summary>
・ 회원은 기존 비밀번호 입력 후 새 비밀번호로 변경할 수 있다.
<img src="/assets/images/readme/gif/password.gif"/>
</details>

<br>

### ⭐ 카테고리

<details>
<summary>카테고리 조회/추가</summary>
・ 회원은 자신이 등록한 카테고리를 조회할 수 있다. <br>
・ 회원은 일정 관리를 위한 카테고리에 대해 카테고리명, 색상을 등록 할 수 있다.
<img src="/assets/images/readme/gif/createcategory.gif"/>
</details>

<br>

<details>
<summary>카테고리 수정</summary>
・ 회원은 기존 카테고리의 이름이나 색상 등을 수정할 수 있다.
<img src="/assets/images/readme/gif/updatecategory.gif"/>
</details>

<br>

<details>
<summary>카테고리 삭제</summary>
・ 회원은 사용하지 않는 카테고리를 삭제할 수 있다.
<img src="/assets/images/readme/gif/deletecategory.gif"/>
</details>

<br>

<details>
<summary>카테고리 완료</summary>
・ 회원은 등록한 카테고리가 완료될 경우 해당 카테고리를 완료처리 할 수 있다.
<img src="/assets/images/readme/gif/categorycompletionStatus.gif"/>
</details>

<br>

### ⭐ 할 일

<details>
<summary>일정 추가</summary>
・ 회원은 일정명, 날짜, 카테고리를 입력하여 일정을 추가할 수 있다.
<img src="/assets/images/readme/gif/createschedule.gif"/>
</details>

<br>

<details>
<summary>일정 조회</summary>
・ 회원은 달력 또는 리스트 형태로 일정을 조회할 수 있다.
<img src="/assets/images/readme/gif/schedulelist.gif"/>
</details>

<br>

<details>
<summary>일정 완료 등록/취소</summary>
・ 회원은 일정을 완료 상태로 변경할 수 있다. <br>
・ 회원은 완료 처리된 일정을 미완료 상태로 되돌릴 수 있다.
<img src="/assets/images/readme/gif/completionStatus.gif"/>
</details>

<br>

### ⭐ 할 일 자동화

<details>
<summary>일정 자동화 추가/조회</summary>
・ 회원은 반복 일정 등 자동화 규칙을 추가/조회할 수 있다.
<img src="/assets/images/readme/gif/createauto.gif"/>
</details>

<br>

<details>
<summary>일정 자동화 수정</summary>
・ 회원은 등록된 자동화 규칙을 수정할 수 있다.
<img src="/assets/images/readme/gif/updateauto.gif"/>
</details>

<br>

### ⭐ 알림

<details>
<summary>일별 미완료 건 알림</summary>
・ 회원에게 미완료한 일정에 대한 알림을 제공할 수 있다.
<img src="/assets/images/readme/gif/alarm.gif"/>
</details>

<br>

<details>
<summary>달성률 알림</summary>
・ 회원에게 주간, 월간 사용자의 전체적인 달성률에 대한 알림을 제공할 수 있다.
<img src="/assets/images/readme/gif/achievement.gif"/>
</details>

<br>

### ⭐ 피드백
<details>
<summary>피드백 도출/조회</summary>
・ 시스템은 매월 1일 전월의 회고를 기반으로 피드백을 도출할 수 있다.
・ 회원은 월별 피드백을 조회할 수 있다.
<img src="/assets/images/readme/gif/feedback.gif"/>
</details>

<br>

### ⭐ 통계
<details>
<summary>일별/월별/카테고리별 통계 조회</summary>
・ 회원은 일별/월별/카테고리별 할 일 달성률을 기준으로 통계를 조회할 수 있다.
<img src="/assets/images/readme/gif/statistics.gif"/>
</details>

<br>


### ⭐ 회고 관리


<details>
<summary>회고 작성</summary>
・ 회원은 제목과 내용의 형태로 작성 회고를 작성 할 수 있다.
<img src="/assets/images/readme/gif/createdaily.gif"/>
</details>

<br>

<details>
<summary>회고 수정</summary>
・ 회원은 작성한 회고는 수정 할 수 있다.
<img src="/assets/images/readme/gif/updatedaily.gif"/>
</details>

<br>

<details>
<summary>회고 삭제</summary>
・ ・회원은 작성된 회고를 삭제 할 수 있다.
<img src="/assets/images/readme/gif/deletedaily.gif"/>
</details>

<br>

---

## 10. 빌드 및 배포 문서📦
<h2>Dockerfile</h2>
<details> 

 <summary> </summary>


``` YML

FROM openjdk:17

LABEL authors="hyeon-young"

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

```

``` YML

FROM node:22-alpine AS build-stage

LABEL authors="hyeon-young"

WORKDIR /app

# 변경이 없으면 캐시를 재사용하여 npm install 생략 가능
COPY package.json package-lock.json ./

RUN npm install

COPY . .

RUN npm run build

# nginx 이미지를 사용하여 프로덕션 스테이지를 구성
FROM nginx:stable-alpine AS production-stage

# 빌드 스테이지에서 생성된 정적 파일들을 Nginx의 기본 웹 서버 디렉토리로 복사
# (Nginx가 Vue.js 애플리케이션의 빌드된 파일들을 서빙)
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 로컬 디렉토리의 nginx.conf 파일을 Nginx의 설정 디렉토리로 복사
COPY ./nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# Nginx를 포그라운드에서 실행하여 컨테이너가 계속 실행
CMD ["nginx", "-g", "daemon off;"]

```

</details>
<br>

<h2>Jenkins Pipeline Script</h2>
<details>
<summary> </summary>

```groovy

pipeline {
    agent any

    tools {
        gradle 'gradle'
        jdk 'openJDK17'
        nodejs 'nodejs'
    }

    environment {
        GITHUB_URL = 'https://github.com/memory-h/be15-4th-PigMa-HaruSari-Jenkins'
        GIT_USERNAME = 'memory-h'
        GIT_EMAIL = 'gusdud9124@gmail.com'
        HTTP_PROXY = ''
        HTTPS_PROXY = ''
        NO_PROXY = 'localhost,127.0.0.1'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker --version'
                    } else {
                        bat 'docker --version'
                    }
                }
            }
        }

        stage('Source Build') {
            steps {
                git branch: 'main', url: "${env.GITHUB_URL}"

                script {
                    dir('frontend') {
                        if (isUnix()) {
                            sh 'npm install'
                            sh 'npm run build'
                        } else {
                            bat 'npm install'
                            bat 'npm run build'
                        }
                    }

                    dir('backend') {
                        if (isUnix()) {
                            sh 'chmod +x ./gradlew'
                            sh './gradlew clean build -x test'
                        } else {
                            bat 'gradlew.bat clean build -x test'
                        }
                    }
                }
            }
        }

        stage('Container Build and Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {

                        if (isUnix()) {
                            sh "docker build -t ${DOCKER_USER}/harusari-frontend:${currentBuild.number} -f frontend/Dockerfile frontend"
                            sh "docker tag ${DOCKER_USER}/harusari-frontend:${currentBuild.number} ${DOCKER_USER}/harusari-frontend:latest"
                            sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                            sh "docker push ${DOCKER_USER}/harusari-frontend:${currentBuild.number}"
                            sh "docker push ${DOCKER_USER}/harusari-frontend:latest"

                            sh "docker build -t ${DOCKER_USER}/harusari-backend:${currentBuild.number} -f backend/Dockerfile backend"
                            sh "docker tag ${DOCKER_USER}/harusari-backend:${currentBuild.number} ${DOCKER_USER}/harusari-backend:latest"
                            sh "docker push ${DOCKER_USER}/harusari-backend:${currentBuild.number}"
                            sh "docker push ${DOCKER_USER}/harusari-backend:latest"
                        } else {
                            bat "docker build -t %DOCKER_USER%/harusari-frontend:${env.BUILD_NUMBER} -f frontend/Dockerfile frontend"
                            bat "docker tag %DOCKER_USER%/harusari-frontend:${env.BUILD_NUMBER} %DOCKER_USER%/harusari-frontend:latest"
                            bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
                            bat "docker push %DOCKER_USER%/harusari-frontend:${env.BUILD_NUMBER}"
                            bat "docker push %DOCKER_USER%/harusari-frontend:latest"

                            bat "docker build -t %DOCKER_USER%/harusari-backend:${env.BUILD_NUMBER} -f backend/Dockerfile backend"
                            bat "docker tag %DOCKER_USER%/harusari-backend:${env.BUILD_NUMBER} %DOCKER_USER%/harusari-backend:latest"
                            bat "docker push %DOCKER_USER%/harusari-backend:${env.BUILD_NUMBER}"
                            bat "docker push %DOCKER_USER%/harusari-backend:latest"
                        }
                    }
                }
            }
        }

        stage('K8S Manifest Update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                        if (isUnix()) {
                            sh "git config --global user.name '${env.GIT_USERNAME}'"
                            sh "git config --global user.email '${env.GIT_EMAIL}'"
                            sh "rm -rf repo && git clone https://${GIT_USER}:${GIT_PASS}@github.com/memory-h/be15-4th-PigMa-HaruSari-Jenkins.git repo"
                            dir('repo') {
                                sh "sed -i '' 's|harusari-frontend:.*|harusari-frontend:${currentBuild.number}|' k8s/vue/harusari-vue-dep.yml"
                                sh "sed -i '' 's|harusari-backend:.*|harusari-backend:${currentBuild.number}|' k8s/boot/harusari-boot-dep.yml"
                                sh "git add ."
                                sh "git commit -m '[UPDATE] ${currentBuild.number} image versioning' || echo 'No changes to commit.'"
                                sh "git push origin main"
                            }
                        } else {
                            bat "git config --global user.name '${env.GIT_USERNAME}'"
                            bat "git config --global user.email '${env.GIT_EMAIL}'"
                            bat "rmdir /s /q repo && git clone https://${GIT_USER}:${GIT_PASS}@github.com/memory-h/be15-4th-PigMa-HaruSari-Jenkins.git repo"
                            dir('repo') {
                                bat "powershell -Command \"(Get-Content k8s/vue/harusari-vue-dep.yml) -replace 'harusari-frontend:.*', 'harusari-frontend:${env.BUILD_NUMBER}' | Set-Content k8s/vue/harusari-vue-dep.yml\""
                                bat "powershell -Command \"(Get-Content k8s/boot/harusari-boot-dep.yml) -replace 'harusari-backend:.*', 'harusari-backend:${env.BUILD_NUMBER}' | Set-Content k8s/boot/harusari-boot-dep.yml\""
                                bat "git add ."
                                bat "git commit -m \"[UPDATE] ${env.BUILD_NUMBER} image versioning\" || echo 'No changes to commit.'"
                                bat "git push origin main"
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh 'docker logout'
                } else {
                    bat 'docker logout'
                }
            }
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

```

</details>

<br>


---

## 11. Jenkins CI/CD 테스트 결과
<br>
<img src="/assets/images/readme/gif/jenkins_pipe2.gif"/>
<br>

---

## 12. 💌 팀원 회고


|**이기연**|
|------|

>이번 프로젝트에서는 Spring Boot와 Vue.js 기반의 To-do 서비스에 SSE와 RabbitMQ를 활용한 실시간 알림 기능을 구현했습니다. 또한 Gemini LLM을 활용하여 사용자의 일정과 피드백을 분석하고 AI가 피드백을 제공하는 기능도 개발했습니다. 알림은 CQRS 패턴과 스케줄링을 통해 매일, 매주, 매월 자동 전송되도록 구성했습니다. 개발 시간이 부족해 모든 기능을 완벽히 구현하지는 못했지만, 주어진 시간 내에 최선을 다해 개발을 진행했습니다. Jenkins 기반의 CI/CD 파이프라인을 구축하는 과정에서는 GitHub Access Token 오류, WSL 미지원 등 윈도우 환경에서 여러 문제를 겪었고, 해결하기 위해 다양한 시도를 함으로써 많은 경험을 했습니다. 다양한 기술을 직접 적용하며 백엔드 전반에 대한 이해도를 높일 수 있었습니다. 특히 팀원들의 적극적인 도움을 받아 어려운 상황을 함께 해결하며 협업의 중요성을 깊이 느꼈습니다.

<br>


|**이주미**|
|------|

>이번 프로젝트에서 저는 회원 도메인 전반을 맡아 개발을 진행했습니다. Spring Security와 JWT 기반의 인증·인가 구조를 설계하고, 일반 회원가입/로그인뿐 아니라 카카오 OAuth2 소셜 로그인까지 모두 직접 구현하였습니다. 이전 프로젝트에서는 주로 기능 단위의 구현에 집중했다면, 이번에는 보안 구조 설계와 세션 관리, 토큰 기반 인증 흐름 전반을 이해하고 구현하는 데 의미 있는 시간을 보낼 수 있었습니다. 백엔드에서는 access token과 refresh token을 각각 분리해 관리하고, refresh token은 Redis에 저장하여 서버 측 인증 유지에 필요한 인프라까지 다루었습니다. 프론트엔드에서는 Vue + Pinia 구조 하에 JWT 기반 인증 상태를 클라이언트 측에서 관리하고, 자동 로그인 및 토큰 만료 시점에서의 refresh 흐름까지 구현했습니다. 특히 쿠키 기반 refresh token 처리와 CORS 이슈를 해결하는 과정에서 보안 구조에 대해 더 고민해보고 공부해볼 수 있었던 것이 의미있었습니다. CI/CD 프로젝트인 만큼 CI/CD 영역에도 기여하고자 했으나, GitHub Organization 내부의 private repository에 대한 Deploy Key 인증 문제로 인해 Jenkins 파이프라인 이후 단계로 넘어가지 못했던 점이 가장 아쉬운 점으로 남는 것 같습니다. 다음 프로젝트에서는 CI/CD와 인프라까지 직접 연계해볼 수 있도록 이 경험을 발판 삼아 더욱 성장하고 싶습니다.

<br>


|**장건희**|
|------|

>이번 프로젝트는 개발이라는 단계를 거치기 전부터 하나하나 구상하면서 그간의 프로젝트에서 챙기지 못햇던 컨벤션이나 협업에 대한 룰들에 대해 더욱 자세하게 정리하고 시도해볼 수 있는 프로젝트였습니다. 이번 프로젝트에서는 카테고리와 회고에 대한 부분을 담당하게 됐지만 프론트를 전적으로 담당하게 되면서 더욱 백엔드의 코드에 더 깊게 접근할 수 있어 알림에서의 MQ와 SSE , 그리고 JWT 토큰을 이용한 인증 방식 등에 대해서도 자세히 알아 볼 수 있는 기회였던 것 같습니다.팀원들과 쿠버네티스와 도커를 활용하여 Jenkins를 통해 CI/CD까지 구현하고자 하였으나 Github Access Token으로 인한 인증에서 문제가 발생하여 Oranization에서의 CI/CD까지는 구현하지 못하였으나 이번 프로젝트에서 배운 내용으로 다음 프로젝트에서는 더욱 완성도 있는 프로젝트를 완성 할 수 있을 것 같습니다. 짧은 기간동안 많이 바쁘고 정신 없었지만 각자의 자리에서 최선을 다해준 팀원들과의 협업에서 얻은 점이 더욱 많은 프로젝트였던 것 같습니다.

<br>


|**장현영**|
|------|

>이번 프로젝트에서는 기능 개발보다는 Docker와 Kubernetes를 실제로 적용해보는 데 집중했습니다. Kubernetes 클러스터를 구성하고 서비스를 직접 배포해보면서, 그동안 개념으로만 알고 있던 내용을 손으로 하나하나 구현해보며 확실히 이해할 수 있는 계기가 되었습니다. Docker Desktop에서 제공하는 단일 노드 Kubernetes 클러스터를 기반으로, 프론트(Vue + Nginx), 백엔드(Spring Boot), MariaDB, Redis, RabbitMQ까지 배포했습니다. 각각의 서비스는 Deployment 또는 StatefulSet으로 관리했고, Ingress Controller를 함께 구성하여 Ingress를 통해 외부에서 접근할 수 있도록 설정했습니다. 특히 좋았던 점은, 아키텍처 구조를 직접 시각화하면서 Deployment, Service, Ingress, PVC 같은 리소스들이 서로 어떻게 연결되고 동작하는지 머릿속에 명확히 그릴 수 있었습니다. 구조도를 직접 그려보며, 단순히 리소스를 나열하고 연결하는 수준을 넘어서 Service와 Ingress의 역할, Secret의 필요성, PVC와 PV를 통한 데이터 보존 방식까지 자연스럽게 이해할 수 있었습니다. 아쉬웠던 점은, 파드의 수를 더 늘려보고 싶었지만 Kubernetes 환경이 제대로 동작하지 않는 문제가 있었고, 데이터베이스의 경우도 파드를 1개만 설정해 사용했기 때문에 여러 개의 파드를 두고 데이터베이스 간 동기화까지 해보지 못한 것이 아쉬웠습니다.

<br>


|**최지혜**|
|------|

> 이번 프로젝트에서 전보다 다양한 경험을 해볼 수 있어 많이 배울 수 있었습니다. 특히 깃 허브에 익숙하지 않았는데 이슈 생성과 코드 리뷰, 리드미 작성 등을 해보면서 익숙해지는 기회를 가질 수 있었고 테스트 코드도 작성해 보며 코드에 대한 이해나 안정성에 대해 학습할 수 있었습니다.
이전보다 코드의 흐름을 파악하고 오류를 찾는 데 조금 수월함을 느꼈고 느리지만 조금씩 성장해나가고 있음을 느껴 더 열심히 할 수 있었습니다.
든든한 팀원들 덕분에 즐겁게 프로젝트를 마무리할 수 있었습니다. 특히 팀원들 모두 질문에 대한 답변을 너무 알아듣기 쉽게 잘 설명해 주고 분담한 업무 믿고 맡겨줘 너무 고마웠습니다.  최종 프로젝트에서는 또 조금 더 성장한 모습을 보일 수 있도록 더 열심히 하겠습니다. 감사합니다.

<br>

---