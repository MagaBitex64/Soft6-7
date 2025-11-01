public class GameLogger implements GameObserver {
    public void onAttack(String attacker, String defender, int dmg, String type) {
        System.out.println(attacker + " hit " + defender + " for " + dmg);
    }

    public void onHealthChange(String heroName, int hp, int maxHp) {
    }

    public void onDeath(String heroName) {
    }

    public void onStrategyChange(String heroName, String newStrat) {
        System.out.println(heroName + " changed to " + newStrat);
    }

    public void onMove(String heroName, Position pos) {
    }

    public void onSplash(String heroName, int dmg) {
        System.out.println(heroName + " took splash " + dmg);
    }

    public void onOutOfRange(String attacker, String defender, double dist, int rng) {
        System.out.println("out of range");
    }
}