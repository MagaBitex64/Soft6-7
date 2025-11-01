import java.util.ArrayList;
import java.util.List;

class Hero {
    private String name;
    private String heroClass;
    private int hp, maxHp;
    private AttackStrategy atk;
    private List<GameObserver> obs = new ArrayList<>();
    private boolean alive = true;
    private Position pos;

    public Hero(String name, String heroClass, int maxHp, AttackStrategy atk, Position pos) {
        this.name = name;
        this.heroClass = heroClass;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.atk = atk;
        this.pos = pos;
    }

    public void addObserver(GameObserver o) {
        obs.add(o);
    }

    public void removeObserver(GameObserver o) {
        obs.remove(o);
    }

    private void notifyAtk(String def, int dmg, String type) {
        for (GameObserver o : obs)
            o.onAttack(this.name, def, dmg, type);
    }

    private void notifyHp() {
        for (GameObserver o : obs)
            o.onHealthChange(this.name, this.hp, this.maxHp);
    }

    private void notifyDeath() {
        for (GameObserver o : obs)
            o.onDeath(this.name);
    }

    private void notifyStrat(String strat) {
        for (GameObserver o : obs)
            o.onStrategyChange(this.name, strat);
    }

    private void notifyMove(Position p) {
        for (GameObserver o : obs)
            o.onMove(this.name, p);
    }

    private void notifySplash(int dmg) {
        for (GameObserver o : obs)
            o.onSplash(this.name, dmg);
    }

    private void notifyRange(String def, double dist, int rng) {
        for (GameObserver o : obs)
            o.onOutOfRange(this.name, def, dist, rng);
    }

    public void changeAttack(AttackStrategy strat, String stratName) {
        this.atk = strat;
        notifyStrat(stratName);
    }

    public void move(int x, int y) {
        this.pos.setX(x);
        this.pos.setY(y);
        notifyMove(this.pos);
    }

    public void attack(Hero target, Hero nearby) {
        if (!this.alive) {
            System.out.println(this.name + " cannot attack - they are defeated!");
            return;
        }

        if (!target.isAlive()) {
            System.out.println(target.getName() + " is already defeated!");
            return;
        }

        double dist = this.pos.distanceTo(target.getPos());
        int rng = atk.range();

        if (dist > rng) {
            notifyRange(target.getName(), dist, rng);
            return;
        }

        int dmg = atk.attack();
        String desc = atk.getDescription();

        notifyAtk(target.getName(), dmg, desc);
        target.damage(dmg);

        if (atk instanceof MagicAttack && nearby != null && nearby.isAlive()) {
            double nearDist = target.getPos().distanceTo(nearby.getPos());
            if (nearDist <= 2) {
                MagicAttack magic = (MagicAttack) atk;
                int splash = magic.splashDmg();
                nearby.splashDamage(splash);
            }
        }
    }

    public void damage(int dmg) {
        this.hp -= dmg;
        if (this.hp < 0) this.hp = 0;

        notifyHp();

        if (this.hp == 0 && this.alive) {
            this.alive = false;
            notifyDeath();
        }
    }

    public void splashDamage(int dmg) {
        notifySplash(dmg);
        damage(dmg);
    }

    public String getName() { return name + " the " + heroClass; }
    public boolean isAlive() { return alive; }
    public int getHp() { return hp; }
    public Position getPos() { return pos; }
    public int getRange() { return atk.range(); }
}