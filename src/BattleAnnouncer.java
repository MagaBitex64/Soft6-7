public class BattleAnnouncer implements GameObserver {
    public void onAttack(String attacker, String defender, int dmg, String type) {
        System.out.println(attacker + " " + type + " -> " + defender + " (" + dmg + " dmg)");
    }

    public void onHealthChange(String heroName, int hp, int maxHp) {
        System.out.println(heroName + ": " + hp + "/" + maxHp + " HP");
    }

    public void onDeath(String heroName) {
        System.out.println(heroName + " defeated");
    }

    public void onStrategyChange(String heroName, String newStrat) {
        System.out.println(heroName + " -> " + newStrat);
    }

    public void onMove(String heroName, Position pos) {
        System.out.println(heroName + " moved to " + pos);
    }

    public void onSplash(String heroName, int dmg) {
        System.out.println(heroName + " splash dmg " + dmg);
    }

    public void onOutOfRange(String attacker, String defender, double dist, int rng) {
        System.out.println(attacker + " too far from " + defender + " (" + String.format("%.1f", dist) + "/" + rng + ")");
    }
}