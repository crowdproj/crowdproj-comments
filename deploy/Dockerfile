# Pull the minimal Ubuntu image
FROM eclipse-temurin:17-jre-alpine
MAINTAINER soulofpain.k@gmail.com

# Update OS and install nginx
RUN apk update && apk upgrade && apk add nginx

# Copy the Nginx config
COPY default /etc/nginx/sites-available/default

# Expose the port for access
EXPOSE 80/tcp

# Run the Nginx server
CMD ["/usr/sbin/nginx", "-g", "daemon off;"]