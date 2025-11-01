public class MeleeAttack implements AttackStrategy {
    public int attack() {
        return 15 + (int)(Math.random() * 10);
    }

    public String getDescription() {
        return "swings their sword";
    }

    public int range() {
        return 1;
    }
}