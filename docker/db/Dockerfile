FROM mariadb:11.3

ENV MARIADB_ROOT_PASSWORD=rootpass \
    MARIADB_DATABASE=orangehrm \
    MARIADB_USER=orangeuser \
    MARIADB_PASSWORD=orangepass

COPY seed.sql /docker-entrypoint-initdb.d/01-seed.sql