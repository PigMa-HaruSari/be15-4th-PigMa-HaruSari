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