CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_name UNIQUE (name),
    CONSTRAINT uq_user_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS events (
    annotation VARCHAR(2000) NOT NULL,
    category INTEGER NOT NULL,
    confirmed_requests INTEGER NOT NULL,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    initiator INTEGER NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR(255) NOT NULL,
    title VARCHAR(120) NOT NULL,
    views INTEGER NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT event_category_fk FOREIGN KEY (category)
    REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT event_user_fk FOREIGN KEY (initiator)
    REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS compilations (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(255) NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT uq_compilation_title UNIQUE (title)
    );

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id INTEGER NOT NULL,
    compilation_id INTEGER NOT NULL,
    CONSTRAINT events_fk FOREIGN KEY (event_id)
    REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT compilations_fk FOREIGN KEY (compilation_id)
    REFERENCES compilations (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS requests (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP NOT NULL,
    event INTEGER NOT NULL,
    requester INTEGER NOT NULL,
    state VARCHAR(255) NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT request_event_fk FOREIGN KEY (event)
    REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT request_user_fk FOREIGN KEY (requester)
    REFERENCES users (id) ON DELETE CASCADE
    );
