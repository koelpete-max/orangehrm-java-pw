FROM mariadb:11.3

ENV MYSQL_ROOT_PASSWORD=rootpass \
    MYSQL_DATABASE=orangehrm \
    MYSQL_USER=orangeuser \
    MYSQL_PASSWORD=orangepass

COPY seed.sql /docker-entrypoint-initdb.d/01-seed.sql