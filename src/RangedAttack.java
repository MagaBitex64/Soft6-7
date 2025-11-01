class RangedAttack implements AttackStrategy {
    public int attack() {
        return 10 + (int)(Math.random() * 15);
    }

    public String getDescription() {
        return "fires a precise shot from distance";
    }

    public int range() {
        return 5;
    }
}