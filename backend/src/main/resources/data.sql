INSERT INTO items (id, imageURL, des, cost, own, accuracy, latitude, longitude, sess, typ, effect_type, effect) VALUES
	(1, null, "Extends view range of holder by 10 meters", 5.0, -1, -1, -1, -1, -1, 0, 0, 10.0),
    (2, null, "Extends the kill range of holder by 1 meter", 10.0, -1, -1, -1, -1, -1, 0, 1, 1.0),
    (3, null, "Adds a view radius on the map to search for the holder's targets. 20 meter view range", 20.0, -1, -1, -1, -1, -1, 1, 0, 20.0);

INSERT INTO users (id, auth_level, currency, deaths, kill_radius, kills, levell, accuracy, latitude, longitude, passwordd, authenticator, expiration, username, view_radius) VALUES
	(1, 2, 0, 0, 1, 0, 1, 0, 0, 0, "adminSean123", null, 12345678, "sean", 30),
    (2, 2, 0, 0, 1, 0, 1, 0, 0, 0, "adminCaden123", null, 12345678, "caden", 30),
    (3, 2, 0, 0, 1, 0, 1, 0, 0, 0, "adminJason123", null, 12345678, "jason", 30),
    (4, 2, 0, 0, 1, 0, 1, 0, 0, 0, "adminJeff123", null, 12345678, "jeff", 30);