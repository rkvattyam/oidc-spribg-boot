FROM node:20-alpine AS build


WORKDIR /app


COPY package*.json ./


RUN npm ci

COPY . .

# Build Angular app
RUN npx ng build --configuration production --project client-org

# Stage 2: Serve the app using nginx
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/dist/client-org/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
# Start nginx
CMD ["nginx", "-g", "daemon off;"]
