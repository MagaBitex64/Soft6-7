public class MagicAttack implements AttackStrategy {
    public int attack() {
        return 20 + (int)(Math.random() * 15);
    }

    public String getDescription() {
        return "casts a powerful spell";
    }

    public int range() {
        return 3;
    }

    public int splashDmg() {
        return attack() / 2;
    }
}