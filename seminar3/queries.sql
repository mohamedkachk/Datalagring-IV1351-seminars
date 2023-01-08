-- It Shows the number of lessons given per month during a specified year.
-- It is possible to retrieve the total number of lessons per month (just one number per month)
-- and the specific number of individual lessons, group lessons and ensembles (three numbers per month).
-- It shows all four numbers on the same row (total plus one per lesson type).
-------------------------------------1-----------------------------
CREATE VIEW number_of_lesson_per_month AS
SELECT TO_CHAR(start_time, 'YYYY MONTH') AS date,
COUNT(*) AS all_lessons,
COUNT(*) FILTER (WHERE lesson_type = 'individual') AS individual_lessons,
COUNT(*) FILTER (WHERE lesson_type = 'group') AS group_lessons,
COUNT(*) FILTER (WHERE lesson_type = 'ensemble') AS ensemble_lessons
FROM lesson GROUP BY TO_CHAR(start_time, 'YYYY MONTH');

-------------------------------------------------------------------------------------------
-- It Shows how many students there are with no sibling, with one sibling, with two siblings, etc.
------------------------------2-------------------------------------
CREATE VIEW number_of_sibling AS
SELECT COUNT(r.number_of_sibling) AS number_of_student, r.number_of_sibling
FROM
(SELECT student_id, COUNT(sb.student_id) AS number_of_sibling
FROM
student FULL JOIN sibling sb USING (student_id)
GROUP BY student_id
) AS r
GROUP BY r.number_of_sibling ORDER BY r.number_of_sibling;
------------------------------------------------------------------
-- List all instructors who has given more than a specific number of lessons during the current month.
-------------------------3-----------------------------------------------
SELECT
instructor_id,
COUNT(instructor_id) AS no_of_lesson,
TO_CHAR(start_time,'YYYY MONTH') AS date
FROM
lesson
WHERE TO_CHAR(start_time,'YYYY MM') = to_char(now(),'YYYY MM')
GROUP BY
instructor_id,
TO_CHAR(start_time,'YYYY MONTH')
HAVING COUNT(instructor_id) > 0;
------------------------------------------------------------------------
-- List all ensembles held during the next week, sorted by music genre and weekday.
-- For each ensemble tell whether it's full booked, has 1-2 seats left or has more seats left.
------------------------------------4---------------------------------
CREATE MATERIALIZED VIEW all_ensembles_held_next_week AS
SELECT group_lesson_id AS ensemble_id, genre, max_no_students, no_of_student, time,
 CASE
       WHEN no_of_student = max_no_students THEN 'full'
       WHEN no_of_student = max_no_students - 1 THEN '1 seats left'
       WHEN no_of_student = max_no_students - 2 THEN '2 seats left'
       ELSE 'more seats left'
 END as lefted_seats
FROM
(SELECT * FROM group_lesson AS g
JOIN
(SELECT group_lesson_id, COUNT(*) AS no_of_student
FROM group_student GROUP BY group_lesson_id) AS s
USING(group_lesson_id)) AS g_join_s
JOIN
available_lesson ON scheduled_lesson_id = available_lesson_id
WHERE lesson_type = 'ensemble'
AND date_trunc('week', time) = date_trunc('week', now()) + interval '1 week'
ORDER BY genre;
----------------------------------------------------------------------