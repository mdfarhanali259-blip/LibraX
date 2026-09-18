# System Architecture

```text
+----------------------------+
| Command Line / Swing UI    |
+-------------+--------------+
              |
              v
+----------------------------+
| Service Layer              |
| Library | ReportService    |
+-------------+--------------+
              |
              v
+----------------------------+
| Model Layer                |
| Person | Member | Book     |
| Librarian | Transaction    |
+-------------+--------------+
              |
              v
+----------------------------+
| File Storage Layer         |
| FileHandler + .dat files  |
+----------------------------+
```
