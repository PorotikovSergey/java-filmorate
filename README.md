# java-filmorate
Template repository for Filmorate project.

![Схема БД приложения filmorate] (QuickDBD-Filmorate (2).png)

# примеры запросов:
SELECT *
FROM film
ORDER BY film_releaseDate

SELECT *
FROM user
WHERE user_name = 'Вася'

SELECT user_id
FROM friendship
WHERE status = 'confirmed'

SELECT (count)liked_films.film_id
FROM liked_films
GROUP BY film_id

SELECT (count)liked_films.user_id
FROM liked_films
GROUP BY user_id

SELECT film.film_name
FROM film
WHERE film_id IN (SELECT liked_films.film_id
FROM liked_films
GROUP BY user_id
ORDER BY COUNT(user_id) DESC
LIMIT 10) 