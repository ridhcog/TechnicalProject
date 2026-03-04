package Hospital.demo.model;
class Insurance {
    String policyName;
    int policyAge;
    double premium;

    void createPolicy(String name, int age) {
        policyName = name;
        policyAge = age;
        System.out.println("Policy created for " + policyName + " (Age: " + policyAge + ")");
    }

    void calculatePremium() {
        premium = 5000;
        if (policyAge > 40) {
            premium += premium * 0.20;  // add 20%
        }
        System.out.println("Calculated Premium: ₹" + premium);
    }

    void showPolicy() {
        System.out.println("Policy Holder: " + policyName + ", Age: " + policyAge + ", Premium: ₹" + premium);
    }

    public static void main(String[] args) {
        Insurance ins = new Insurance();
        ins.createPolicy("Ridhwit", 45);
        ins.calculatePremium();
        ins.showPolicy();
    }
}
