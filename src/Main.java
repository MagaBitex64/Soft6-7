import java.util.Scanner;
import java.util.Random;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Random rnd = new Random();

    public static void main(String[] args) {
        GameLogger logger = new GameLogger();
        BattleAnnouncer announcer = new BattleAnnouncer();

        Hero swordsman = new Hero("Lancelot", "Swordsman", 150, new MeleeAttack(), new Position(3, 1));
        Hero mage = new Hero("Xavier", "Mage", 80, new MagicAttack(), new Position(3, 5));
        Hero archer = new Hero("Miya", "Archer", 90, new RangedAttack(), new Position(1, 3));

        swordsman.addObserver(logger);
        swordsman.addObserver(announcer);
        mage.addObserver(logger);
        mage.addObserver(announcer);
        archer.addObserver(logger);
        archer.addObserver(announcer);

        Hero player = pickHero(swordsman, mage, archer);
        Hero e1, e2;

        if (player == swordsman) {
            e1 = mage;
            e2 = archer;
        } else if (player == mage) {
            e1 = swordsman;
            e2 = archer;
        } else {
            e1 = swordsman;
            e2 = mage;
        }

        System.out.println("\nYou: " + player.getName());
        System.out.println("vs " + e1.getName() + " & " + e2.getName());

        showGrid(player, e1, e2);

        int round = 1;
        while (player.isAlive() && (e1.isAlive() || e2.isAlive())) {
            System.out.println("\n-- Round " + round + " --");

            playerTurn(player, e1, e2);

            if (!e1.isAlive() && !e2.isAlive()) break;

            if (e1.isAlive()) {
                botTurn(e1, player, e2);
            }

            if (e2.isAlive() && player.isAlive()) {
                botTurn(e2, player, e1);
            }

            showGrid(player, e1, e2);
            round++;

            try { Thread.sleep(600); } catch (InterruptedException ex) {}
        }

        if (player.isAlive()) {
            System.out.println("\nYou win");
        } else {
            System.out.println("\nYou lost");
        }

        sc.close();
    }

    static void showGrid(Hero p, Hero e1, Hero e2) {
        System.out.println("\n  0 1 2 3 4 5 6");

        for (int y = 0; y < 7; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < 7; x++) {
                if (p.isAlive() && x == p.getPos().getX() && y == p.getPos().getY()) {
                    System.out.print("P ");
                } else if (e1.isAlive() && x == e1.getPos().getX() && y == e1.getPos().getY()) {
                    System.out.print("1 ");
                } else if (e2.isAlive() && x == e2.getPos().getX() && y == e2.getPos().getY()) {
                    System.out.print("2 ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    static Hero pickHero(Hero swordsman, Hero mage, Hero archer) {
        System.out.println("Pick hero:");
        System.out.println("1. " + swordsman.getName() + " (150hp)");
        System.out.println("2. " + mage.getName() + " (80hp, splash)");
        System.out.println("3. " + archer.getName() + " (90hp, long range)");
        System.out.print("> ");

        int choice = getNum(1, 3);

        if (choice == 1) return swordsman;
        if (choice == 2) return mage;
        return archer;
    }

    static void playerTurn(Hero player, Hero e1, Hero e2) {
        System.out.println("\n" + player.getName() + " @ " + player.getPos());

        while (true) {
            System.out.print("1-move 2-attack 3-change 4-status > ");

            int act = getNum(1, 4);

            if (act == 1) {
                if (moveHero(player)) break;
            } else if (act == 2) {
                Hero tgt = pickTarget(player, e1, e2);
                if (tgt != null) {
                    Hero nearby = (tgt == e1) ? e2 : e1;
                    player.attack(tgt, nearby);
                    break;
                }
            } else if (act == 3) {
                changeStrat(player);
            } else {
                showStatus(player, e1, e2);
            }
        }
    }

    static boolean moveHero(Hero h) {
        System.out.print("x (0-6, -1=cancel): ");
        int x = getNum(-1, 6);
        if (x == -1) return false;

        System.out.print("y (0-6, -1=cancel): ");
        int y = getNum(-1, 6);
        if (y == -1) return false;

        double dist = h.getPos().distanceTo(new Position(x, y));
        if (dist > 2) {
            System.out.println("too far");
            return false;
        }

        h.move(x, y);
        return true;
    }

    static Hero pickTarget(Hero atk, Hero e1, Hero e2) {
        int opt = 1;
        if (e1.isAlive()) {
            double d = atk.getPos().distanceTo(e1.getPos());
            System.out.println(opt + ". " + e1.getName() + " " + e1.getHp() + "hp dist=" + String.format("%.1f", d));
            opt++;
        }
        if (e2.isAlive()) {
            double d = atk.getPos().distanceTo(e2.getPos());
            System.out.println(opt + ". " + e2.getName() + " " + e2.getHp() + "hp dist=" + String.format("%.1f", d));
        }

        System.out.print("> ");

        if (e1.isAlive() && e2.isAlive()) {
            int c = getNum(1, 2);
            return c == 1 ? e1 : e2;
        } else if (e1.isAlive()) {
            getNum(1, 1);
            return e1;
        } else if (e2.isAlive()) {
            getNum(1, 1);
            return e2;
        }
        return null;
    }

    static void changeStrat(Hero h) {
        System.out.println("1-melee 2-ranged 3-magic");
        System.out.print("> ");

        int c = getNum(1, 3);

        if (c == 1) {
            h.changeAttack(new MeleeAttack(), "Melee Attack");
        } else if (c == 2) {
            h.changeAttack(new RangedAttack(), "Ranged Attack");
        } else {
            h.changeAttack(new MagicAttack(), "Magic Attack");
        }
    }

    static void showStatus(Hero p, Hero e1, Hero e2) {
        System.out.println(p.getName() + " " + p.getHp() + "hp @ " + p.getPos());
        System.out.println(e1.getName() + " " + (e1.isAlive() ? e1.getHp() + "hp @ " + e1.getPos() : "dead"));
        System.out.println(e2.getName() + " " + (e2.isAlive() ? e2.getHp() + "hp @ " + e2.getPos() : "dead"));
    }

    static void botTurn(Hero ai, Hero player, Hero other) {
        int act = rnd.nextInt(100);

        if (act < 15) {
            int s = rnd.nextInt(3);
            if (s == 0) {
                ai.changeAttack(new MeleeAttack(), "Melee Attack");
            } else if (s == 1) {
                ai.changeAttack(new RangedAttack(), "Ranged Attack");
            } else {
                ai.changeAttack(new MagicAttack(), "Magic Attack");
            }
        } else if (act < 40) {
            int dx = rnd.nextInt(3) - 1;
            int dy = rnd.nextInt(3) - 1;
            int nx = Math.max(0, Math.min(6, ai.getPos().getX() + dx));
            int ny = Math.max(0, Math.min(6, ai.getPos().getY() + dy));
            ai.move(nx, ny);
        } else {
            if (player.isAlive() && rnd.nextInt(100) < 85) {
                Hero near = (other != null && other.isAlive()) ? other : null;
                ai.attack(player, near);
            } else if (other != null && other.isAlive()) {
                ai.attack(other, player.isAlive() ? player : null);
            } else if (player.isAlive()) {
                ai.attack(player, null);
            }
        }
    }

    static int getNum(int min, int max) {
        while (true) {
            try {
                int n = Integer.parseInt(sc.nextLine());
                if (n >= min && n <= max) return n;
                System.out.print("enter " + min + "-" + max + ": ");
            } catch (NumberFormatException ex) {
                System.out.print("number " + min + "-" + max + ": ");
            }
        }
    }
}