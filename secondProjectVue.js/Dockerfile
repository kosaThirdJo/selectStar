FROM node:11.1-alpine as develop-stage
WORKDIR /app
COPY package*.json ./
CMD ["npm","run","install"]
COPY . .
# build stage
FROM develop-stage as build-stage
CMD ["npm","run","build"]
# production stage
FROM nginx as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]