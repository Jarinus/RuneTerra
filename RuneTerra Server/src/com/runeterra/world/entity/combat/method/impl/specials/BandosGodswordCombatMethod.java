package com.runeterra.world.entity.combat.method.impl.specials;

import com.runeterra.util.Misc;
import com.runeterra.world.entity.combat.CombatSpecial;
import com.runeterra.world.entity.combat.CombatType;
import com.runeterra.world.entity.combat.hit.QueueableHit;
import com.runeterra.world.entity.combat.method.CombatMethod;
import com.runeterra.world.entity.impl.Character;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.Animation;
import com.runeterra.world.model.Graphic;
import com.runeterra.world.model.Priority;
import com.runeterra.world.model.Skill;

public class BandosGodswordCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(7060, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1212, Priority.HIGH);

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public QueueableHit[] fetchDamage(Character character, Character target) {
		return new QueueableHit[] { new QueueableHit(character, target, this, true, 0) };
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public void onQueueAdd(Character character, Character target) {
		CombatSpecial.drain(character.getAsPlayer(), CombatSpecial.BANDOS_GODSWORD.getDrainAmount());
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(ANIMATION);
		character.performGraphic(GRAPHIC);
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(QueueableHit hit) {
		if (hit.isAccurate() && hit.getTarget().isPlayer()) {
			int skillDrain = 1;
			int damageDrain = (int) (hit.getTotalDamage() * 0.1);
			if (damageDrain < 0)
				return;
			Player player = hit.getAttacker().getAsPlayer();
			Player target = hit.getTarget().getAsPlayer();
			target.getSkillManager().setCurrentLevel(Skill.forId(skillDrain),
					player.getSkillManager().getCurrentLevel(Skill.forId(skillDrain)) - damageDrain);
			if (target.getSkillManager().getCurrentLevel(Skill.forId(skillDrain)) < 1)
				target.getSkillManager().setCurrentLevel(Skill.forId(skillDrain), 1);
			player.getPacketSender()
					.sendMessage("You've drained " + target.getUsername() + "'s "
							+ Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase()) + " level by "
							+ damageDrain + ".");
			target.getPacketSender().sendMessage("Your "
					+ Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase()) + " level has been drained.");
		}
	}
}