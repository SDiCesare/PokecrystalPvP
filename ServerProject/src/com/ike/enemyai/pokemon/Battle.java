package com.ike.enemyai.pokemon;

public class Battle {


    public enum BattleMode {
        NO_MODE, WILD_BATTLE, TRAINER_BATTLE
    }

    private BattleMode mode;
    private Trainer trainer;
    private EnemyTrainer enemyTrainer;
    private WildMon wildmon;
    private boolean doneLoading;
    private boolean isUpdating;

    public Battle() {
        this.mode = BattleMode.NO_MODE;
        this.doneLoading = false;
        this.isUpdating = false;
    }

    public byte getEnemyMonSpecies() {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getSpecies();
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getSpecies();
        }
        return 0;
    }

    public byte getEnemyMonMove(int index) {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getMove(index);
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getMove(index);
        }
        return 0;
    }

    public byte getEnemyMonMovePP(int index) {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getMovePP(index);
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getMovePP(index);
        }
        return 0;
    }

    public byte getEnemyMonLevel() {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getLevel();
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getLevel();
        }
        return 0;
    }

    public short getEnemyMonHP() {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getHP();
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getHP();
        }
        return 0;
    }

    public short getEnemyMonMaxHP() {
        if (mode == BattleMode.WILD_BATTLE) {
            return wildmon.getMaxHP();
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return enemyTrainer.getActiveMon().getMaxHP();
        }
        return 0;
    }

    public short getTrainerMonHP() {
        return this.trainer.getHP();
    }

    public short getTrainerMonMaxHP() {
        return this.trainer.getMaxHP();
    }

    public void updateBattle(byte b) {
        appendBattleData(b);
    }

    public void append(byte b) {
        if (doneLoading) {
            return;
        }
        if (mode == BattleMode.NO_MODE) {
            if (b == 1) {
                mode = BattleMode.WILD_BATTLE;
                wildmon = new WildMon();
                trainer = new Trainer();
            } else if (b == 2) {
                mode = BattleMode.TRAINER_BATTLE;
                // enemyTrainer = new EnemyTrainer();
                trainer = new Trainer();
            }
        } else {
            appendBattleData(b);
        }
    }

    private void appendBattleData(byte b) {
        if (mode == BattleMode.WILD_BATTLE) {
            if (wildmon.doneLoading()) {
                trainer.append(b);
                if (trainer.doneLoading()) {
                    this.doneLoading = true;
                    this.isUpdating = false;
                }
            } else {
                wildmon.append(b);
            }
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            if (enemyTrainer == null) {
                enemyTrainer = new EnemyTrainer(b);
            } else if (enemyTrainer.doneLoading()) {
                trainer.append(b);
                if (trainer.doneLoading()) {
                    this.doneLoading = true;
                    this.isUpdating = false;
                }
            } else {
                enemyTrainer.append(b);
            }
        }
    }

    public boolean doneLoading() {
        return doneLoading && !isUpdating;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public void setUpdating(boolean updating) {
        isUpdating = updating;
        if (isUpdating) {
            if (mode == BattleMode.WILD_BATTLE) {
                System.out.println("Reset Wild Pokemon");
                wildmon = new WildMon();
                trainer = new Trainer();
            } else if (mode == BattleMode.TRAINER_BATTLE) {
                System.out.println("Reset Enemy Trainer");
                enemyTrainer = null;
                trainer = new Trainer();
            }
        }
    }

    public BattleMode getMode() {
        return mode;
    }

    public EnemyTrainer getEnemyTrainer() {
        return enemyTrainer;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public WildMon getWildmon() {
        return wildmon;
    }

    @Override
    public String toString() {
        if (mode == BattleMode.WILD_BATTLE) {
            return this.wildmon.toString() + "," + this.trainer.toString();
        } else if (mode == BattleMode.TRAINER_BATTLE) {
            return this.enemyTrainer.toString() + "," + this.trainer.toString();
        } else {
            return super.toString();
        }
    }
}
