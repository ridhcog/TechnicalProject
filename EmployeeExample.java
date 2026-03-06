package ecom.demo.model;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeExample {

    enum TaskStatus { TODO, IN_PROGRESS, DONE }
    enum Priority { LOW, MEDIUM, HIGH }

    static class Task {
        private final int taskId;
        private String title;
        private TaskStatus status;
        private Priority priority;
        private LocalDate dueDate;

        public Task(int taskId, String title, TaskStatus status, Priority priority, LocalDate dueDate) {
            this.taskId = taskId;
            this.title = title;
            this.status = status;
            this.priority = priority;
            this.dueDate = dueDate;
        }

        public int getTaskId() { return taskId; }
        public String getTitle() { return title; }
        public TaskStatus getStatus() { return status; }
        public Priority getPriority() { return priority; }
        public LocalDate getDueDate() { return dueDate; }

        public void setTitle(String t) { this.title = t; }
        public void setStatus(TaskStatus s) { this.status = s; }
        public void setPriority(Priority p) { this.priority = p; }
        public void setDueDate(LocalDate d) { this.dueDate = d; }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Task)) return false;
            return taskId == ((Task) o).taskId;
        }

        public int hashCode() { return Objects.hash(taskId); }

        public String toString() {
            return "Task{id=" + taskId + ", title='" + title + "', status=" + status +
                    ", priority=" + priority + ", due=" + dueDate + "}";
        }
    }

    static class Employee {
        private final int empId;
        private String name;
        private Set<Task> tasks = new LinkedHashSet<>();

        public Employee(int empId, String name) {
            this.empId = empId;
            this.name = name;
        }

        public int getEmpId() { return empId; }
        public String getName() { return name; }

        public boolean addTask(Task t) { return tasks.add(t); }
        public Set<Task> getTasks() { return tasks; }

        public Optional<Task> find(int id) {
            return tasks.stream().filter(x -> x.getTaskId() == id).findFirst();
        }

        public String toString() { return "Employee{id=" + empId + ", name='" + name + "'}"; }
    }

    public static void main(String[] args) {

        Map<Integer, Employee> employees = new LinkedHashMap<>();

        Employee e1 = new Employee(101, "Asha");
        Employee e2 = new Employee(102, "Vikram");
        Employee e3 = new Employee(103, "Neha");

        employees.put(e1.getEmpId(), e1);
        employees.put(e2.getEmpId(), e2);
        employees.put(e3.getEmpId(), e3);

        Task t1 = new Task(1, "Build API", TaskStatus.TODO, Priority.HIGH, LocalDate.now().plusDays(5));
        Task t2 = new Task(2, "Write Tests", TaskStatus.TODO, Priority.MEDIUM, LocalDate.now().plusDays(7));
        Task t3 = new Task(3, "Prepare SRS", TaskStatus.IN_PROGRESS, Priority.MEDIUM, LocalDate.now().plusDays(10));
        Task t4 = new Task(4, "Refactor Code", TaskStatus.TODO, Priority.LOW, LocalDate.now().plusDays(12));

        e1.addTask(t1);
        e1.addTask(t4);
        e2.addTask(t2);
        e3.addTask(t3);
        e1.addTask(t1);

        e1.find(1).ifPresent(x -> x.setStatus(TaskStatus.IN_PROGRESS));

        List<Task> filtered = e1.getTasks()
                .stream()
                .filter(x -> x.getStatus() == TaskStatus.IN_PROGRESS)
                .collect(Collectors.toList());

        System.out.println("Filtered Tasks for Employee 101:");
        filtered.forEach(System.out::println);

        System.out.println("\nTasks Grouped by Employees:");
        for (Employee e : employees.values()) {
            System.out.println(e);
            for (Task t : e.getTasks()) {
                System.out.println("  " + t);
            }
        }
    }
}
