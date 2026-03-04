import java.util.*;

class Patient {
    private static int nextId = 1;
    private final int id;
    private final String name;
    private final int age;
    private final String ailment;
    private boolean admitted;

    public Patient(String name, int age, String ailment) {
        this.id = nextId++;
        this.name = name;
        this.age = age;
        this.ailment = ailment;
        this.admitted = true;
    }

    public int getId() { return id; }
    public boolean isAdmitted() { return admitted; }
    public void discharge() { this.admitted = false; }

    @Override
    public String toString() {
        return "Patient{id=" + id + ", name='" + name + "', age=" + age +
                ", ailment='" + ailment + "', admitted=" + admitted + "}";
    }
}

class HospitalService {
    private final Map<Integer, Patient> patients = new HashMap<>();

    public Patient registerPatient(String name, int age, String ailment) {
        validate(name != null && !name.isBlank(), "Name is required.");
        validate(age > 0, "Age must be positive.");
        validate(ailment != null && !ailment.isBlank(), "Ailment is required.");
        Patient p = new Patient(name.trim(), age, ailment.trim());
        patients.put(p.getId(), p);
        return p;
    }

    public Patient getPatient(int id) {
        validate(patients.containsKey(id), "Patient not found with id: " + id);
        return patients.get(id);
    }

    public void dischargePatient(int id) {
        Patient p = getPatient(id);
        validate(p.isAdmitted(), "Patient already discharged.");
        p.discharge();
    }

    public List<Patient> listPatients() {
        return new ArrayList<>(patients.values());
    }

    private void validate(boolean ok, String msg) {
        if (!ok) throw new IllegalArgumentException(msg);
    }
}

public class HospitalApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HospitalService service = new HospitalService();
        boolean running = true;
        System.out.println("🏥 Hospital Management");

        while (running) {
            System.out.println("\n1) Register Patient\n2) View Patient\n3) Discharge Patient\n4) List Patients\n5) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Age: ");
                        int age = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Ailment: ");
                        String ailment = sc.nextLine();
                        Patient p = service.registerPatient(name, age, ailment);
                        System.out.println("Registered: " + p);
                        break;
                    case "2":
                        System.out.print("Patient ID: ");
                        int id2 = Integer.parseInt(sc.nextLine().trim());
                        System.out.println(service.getPatient(id2));
                        break;
                    case "3":
                        System.out.print("Patient ID: ");
                        int id3 = Integer.parseInt(sc.nextLine().trim());
                        service.dischargePatient(id3);
                        System.out.println("Discharged patient id " + id3);
                        break;
                    case "4":
                        service.listPatients().forEach(System.out::println);
                        break;
                    case "5":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Error" + ex.getMessage());
            }
        }
        sc.close();
    }
}
