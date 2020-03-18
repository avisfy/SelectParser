**SelectParser - парсер SQL запроса SELECT.**

Поддержка: 
  + подзапросы в SELECT, FROM, WHERE и HAVING (после знака), выделяются круглыми собками
  + перечисление полей явно (с указанием таблицы-источника и без, в том числе и с alias, формат: column_name AS alias)
  + все поля (*)
  + агрегатные функции полей (AVG, MIN, MAX, COUNT, SUM), так же с/без alias
  + таблицы и их объединения (INNER JOIN, LEFT JOIN, RIGHT JOIN, FULL JOIN, а так же неявное объединение через запятую)
  + условия в WHERE и  HAVING
  + group by 
  + order by (явное указание типа сортировки: asc, desc b; неявное: по умолчанию - asc
  + усечение выборки (LIMIT число, OFFSET число).
  
  Конец запроса - ';'.
  
  
