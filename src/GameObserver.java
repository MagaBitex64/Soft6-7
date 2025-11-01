interface GameObserver {
    void onAttack(String attacker, String defender, int dmg, String type);
    void onHealthChange(String heroName, int hp, int maxHp);
    void onDeath(String heroName);
    void onStrategyChange(String heroName, String newStrat);
    void onMove(String heroName, Position pos);
    void onSplash(String heroName, int dmg);
    void onOutOfRange(String attacker, String defender, double dist, int rng);
}