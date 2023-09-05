# Pull the minimal Ubuntu image
FROM ubuntu
MAINTAINER soulofpain.k@gmail.com

# Update OS
RUN apt-get -y update

# Install Nginx
RUN apt-get -y install nginx

# Copy the Nginx config
COPY default /etc/nginx/sites-available/default

# Expose the port for access
EXPOSE 80/tcp

# Run the Nginx server
CMD ["/usr/sbin/nginx", "-g", "daemon off;"]