sh:mkdir /home/yangmz
sh:chmod 750 /home/yangmz
sh:mkdir /home/yangmz/dist
sh:chmod 750 /home/yangmz/dist

sftp:D:/IdeaProjects/com.yangmz/web/target/web-1.0-SNAPSHOT.war /home/yangmz
sftp:D:/IdeaProjects/com.yangmz/sso/target/sso-1.0-SNAPSHOT.war /home/yangmz

sftp:D:/WebstormProjects/yangmz/dist/favicon.ico /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/main.bundle.js /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/styles.bundle.js /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/styles.bundle.js.map /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/main.bundle.js.map /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/polyfills.bundle.js /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/vendor.bundle.js /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/polyfills.bundle.js.map /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/vendor.bundle.js.map /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/index.html  /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/vendor.bundle.js.map /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/inline.bundle.js /home/yangmz/dist
sftp:D:/WebstormProjects/yangmz/dist/inline.bundle.js.map    /home/yangmz/dist

sh:rm -rf /home/java-Deploy/apache-tomcat-8.5.23/webapps
sh:/home/java-Deploy/apache-tomcat-8.5.23/bin/shutdown.sh
sh:/home/java-Deploy/apache-tomcat-8.5.23/bin/startup.sh