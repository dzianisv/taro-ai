package com.example.data

object LocalTarotInterpreter {
    private val meanings = mapOf(
        "The Fool" to CardMeaning(
            upright = "New beginnings, optimism, trust in life's journey, spontaneity.",
            advice = "Embrace the unknown. Take a leap of faith, but stay mindful of your surroundings."
        ),
        "The Magician" to CardMeaning(
            upright = "Manifestation, resourcefulness, power, inspired action.",
            advice = "You have all the tools you need to succeed. Channel your focus and manifest your goals."
        ),
        "The High Priestess" to CardMeaning(
            upright = "Intuition, sacred knowledge, divine feminine, the subconscious mind.",
            advice = "Look within. Your intuition is whispering to you; trust your dreams and gut feelings."
        ),
        "The Empress" to CardMeaning(
            upright = "Abundance, creativity, nature, nurturing, fertility.",
            advice = "Connect with the earth and your senses. Nurture your creative ideas and let them flourish."
        ),
        "The Emperor" to CardMeaning(
            upright = "Authority, structure, solid foundations, protective power.",
            advice = "Establish order and boundaries. Act with confidence and bring structure to your chaos."
        ),
        "The Hierophant" to CardMeaning(
            upright = "Tradition, spiritual wisdom, conformity, seeking guidance.",
            advice = "Honor tried-and-true wisdom. This is a time for learning and connecting with legacy systems."
        ),
        "The Lovers" to CardMeaning(
            upright = "Harmony, relationships, choices, alignment of values.",
            advice = "Focus on alignment and balance in relationships. Choose paths that match your core values."
        ),
        "The Chariot" to CardMeaning(
            upright = "Willpower, determination, victory, overcoming obstacles.",
            advice = "Stay disciplined and focused. Direct your opposing forces toward a singular, victorious goal."
        ),
        "Strength" to CardMeaning(
            upright = "Inner fortitude, courage, patience, compassion.",
            advice = "Influence situations through gentle influence and patience rather than brute force."
        ),
        "The Hermit" to CardMeaning(
            upright = "Soul-searching, introspection, inner guidance, solitude.",
            advice = "Take a step back. Spend some quiet time in contemplation to find the answers you seek."
        ),
        "Wheel of Fortune" to CardMeaning(
            upright = "Destiny, turning point, luck, inevitable change.",
            advice = "Accept that life is full of cycles. Adapt gracefully to the shifts occurring around you."
        ),
        "Justice" to CardMeaning(
            upright = "Fairness, truth, cause and effect, accountability.",
            advice = "Seek truth and act with integrity. Trust that the scales of karma will eventually balance."
        ),
        "The Hanged Man" to CardMeaning(
            upright = "Surrender, letting go, new perspectives, pause.",
            advice = "Do not force action. View your situation from a different angle and embrace the pause."
        ),
        "Death" to CardMeaning(
            upright = "Endings, transformation, transition, letting go.",
            advice = "Do not fear the end. Clear away the old to make room for powerful new beginnings."
        ),
        "Temperance" to CardMeaning(
            upright = "Balance, moderation, patience, purpose.",
            advice = "Seek the middle path. Combine different areas of your life in harmonious moderation."
        ),
        "The Devil" to CardMeaning(
            upright = "Attachment, restriction, shadow self, temptation.",
            advice = "Recognize where you feel bound. You hold the key to free yourself from unhealthy habits."
        ),
        "The Tower" to CardMeaning(
            upright = "Sudden upheaval, revelation, breakthrough, awakening.",
            advice = "Allow old structures to crumble. It paves the way for building on a firmer foundation."
        ),
        "The Star" to CardMeaning(
            upright = "Hope, faith, renewal, serenity, spiritual glow.",
            advice = "Have faith in the universe. Your future is bright, and healing energies are flowing to you."
        ),
        "The Moon" to CardMeaning(
            upright = "Illusion, fear, anxiety, subconscious depths.",
            advice = "Navigate by your inner light. Things may not be as they seem; let illusions dissolve."
        ),
        "The Sun" to CardMeaning(
            upright = "Vitality, joy, success, warmth, radiant truth.",
            advice = "Celebrate your life! Radiate positivity, clarity, and share your warmth with others."
        ),
        "Judgement" to CardMeaning(
            upright = "Rebirth, absolution, calling, decision-making.",
            advice = "Listen to your higher calling. It is time for self-evaluation and stepping into your power."
        ),
        "The World" to CardMeaning(
            upright = "Completion, integration, travel, ultimate fulfillment.",
            advice = "Celebrate your success. You have completed a major cycle and achieved wholeness."
        ),

        // Minor Arcana — Wands (fire: passion, energy, ambition)
        "Ace of Wands" to CardMeaning(
            upright = "Inspiration, new opportunities, growth, raw creative potential.",
            advice = "Seize the spark. A bold new venture is igniting — act on it with passion."
        ),
        "Two of Wands" to CardMeaning(
            upright = "Future planning, progress, decisions, discovery.",
            advice = "Map your path. You have laid the foundations; now choose your wider horizon."
        ),
        "Three of Wands" to CardMeaning(
            upright = "Expansion, foresight, opportunity abroad, momentum.",
            advice = "Look beyond the familiar. Your plans are in motion — watch them sail out."
        ),
        "Four of Wands" to CardMeaning(
            upright = "Celebration, harmony, home, community, milestones.",
            advice = "Pause to celebrate. Honor a happy stability with the people you love."
        ),
        "Five of Wands" to CardMeaning(
            upright = "Competition, conflict, tension, lively disagreement.",
            advice = "Channel the friction. Healthy rivalry sharpens you — compete, don't quarrel."
        ),
        "Six of Wands" to CardMeaning(
            upright = "Victory, public recognition, confidence, success.",
            advice = "Own your win. Step forward and let your achievements be seen."
        ),
        "Seven of Wands" to CardMeaning(
            upright = "Defensiveness, perseverance, standing your ground.",
            advice = "Hold your position. You have the high ground — defend what you built."
        ),
        "Eight of Wands" to CardMeaning(
            upright = "Speed, swift action, momentum, sudden movement.",
            advice = "Move fast. Events are accelerating — ride the momentum, don't stall."
        ),
        "Nine of Wands" to CardMeaning(
            upright = "Resilience, persistence, a guarded last stand.",
            advice = "One more push. You are closer than you feel — don't give up now."
        ),
        "Ten of Wands" to CardMeaning(
            upright = "Burden, responsibility, hard work, overload.",
            advice = "Set something down. You carry too much — delegate or release the load."
        ),
        "Page of Wands" to CardMeaning(
            upright = "Enthusiasm, exploration, free spirit, discovery.",
            advice = "Follow your curiosity. A playful new interest could become your calling."
        ),
        "Knight of Wands" to CardMeaning(
            upright = "Energy, passion, adventure, impulsive action.",
            advice = "Charge ahead — but aim first. Your fire is powerful when it's focused."
        ),
        "Queen of Wands" to CardMeaning(
            upright = "Courage, confidence, warmth, vibrant independence.",
            advice = "Lead with warmth and self-belief. Your magnetism draws others to you."
        ),
        "King of Wands" to CardMeaning(
            upright = "Vision, leadership, bold action, natural authority.",
            advice = "Take charge of the big picture. Inspire others with a fearless vision."
        ),

        // Minor Arcana — Cups (water: emotion, relationships, intuition)
        "Ace of Cups" to CardMeaning(
            upright = "New love, emotional awakening, compassion, creativity.",
            advice = "Open your heart. A wellspring of feeling and connection is flowing in."
        ),
        "Two of Cups" to CardMeaning(
            upright = "Partnership, mutual attraction, connection, unity.",
            advice = "Meet as equals. A balanced bond — romantic or not — deserves your care."
        ),
        "Three of Cups" to CardMeaning(
            upright = "Friendship, celebration, community, shared joy.",
            advice = "Gather your people. Celebrate together; joy multiplies when shared."
        ),
        "Four of Cups" to CardMeaning(
            upright = "Apathy, contemplation, reevaluation, missed offers.",
            advice = "Look up. Something good is being offered — don't miss it in your boredom."
        ),
        "Five of Cups" to CardMeaning(
            upright = "Loss, grief, disappointment, dwelling on the negative.",
            advice = "Turn around. Two cups still stand — grieve, then reclaim what remains."
        ),
        "Six of Cups" to CardMeaning(
            upright = "Nostalgia, fond memories, innocence, reunion.",
            advice = "Revisit the past kindly. Old connections and simple joys can heal you."
        ),
        "Seven of Cups" to CardMeaning(
            upright = "Choices, illusion, fantasy, wishful thinking.",
            advice = "See clearly. Many options dazzle — choose substance over daydreams."
        ),
        "Eight of Cups" to CardMeaning(
            upright = "Walking away, seeking deeper meaning, transition.",
            advice = "Leave what no longer fills you. A more meaningful path is calling."
        ),
        "Nine of Cups" to CardMeaning(
            upright = "Contentment, satisfaction, gratitude, wishes fulfilled.",
            advice = "Savor this. Your emotional wish is granted — enjoy it fully."
        ),
        "Ten of Cups" to CardMeaning(
            upright = "Harmony, lasting happiness, family, emotional fulfillment.",
            advice = "Cherish your bonds. This is the joy you have been building toward."
        ),
        "Page of Cups" to CardMeaning(
            upright = "Creative beginnings, intuition, sensitivity, curiosity.",
            advice = "Trust a tender feeling. A gentle new idea or message is arriving."
        ),
        "Knight of Cups" to CardMeaning(
            upright = "Romance, charm, idealism, following the heart.",
            advice = "Lead with your heart — but keep both feet grounded. Offer, don't drift."
        ),
        "Queen of Cups" to CardMeaning(
            upright = "Compassion, emotional security, intuition, nurturing.",
            advice = "Care deeply — for others and yourself. Your empathy is a strength."
        ),
        "King of Cups" to CardMeaning(
            upright = "Emotional balance, diplomacy, calm, compassionate control.",
            advice = "Master your feelings without suppressing them. Lead with calm."
        ),

        // Minor Arcana — Swords (air: intellect, truth, conflict)
        "Ace of Swords" to CardMeaning(
            upright = "Breakthrough, clarity, truth, sharp mental power.",
            advice = "Cut through confusion. A clear insight gives you decisive power now."
        ),
        "Two of Swords" to CardMeaning(
            upright = "Indecision, stalemate, a hard choice, blocked emotions.",
            advice = "Remove the blindfold. Face the facts and make the choice you're avoiding."
        ),
        "Three of Swords" to CardMeaning(
            upright = "Heartbreak, painful truth, grief, sorrow.",
            advice = "Let it hurt, then heal. Painful clarity clears the way for recovery."
        ),
        "Four of Swords" to CardMeaning(
            upright = "Rest, recovery, contemplation, a mental pause.",
            advice = "Withdraw and recharge. Your mind needs stillness before the next move."
        ),
        "Five of Swords" to CardMeaning(
            upright = "Conflict, defeat, winning at a cost, tension.",
            advice = "Pick your battles. A hollow victory may cost more than it's worth."
        ),
        "Six of Swords" to CardMeaning(
            upright = "Transition, moving on, leaving trouble behind, recovery.",
            advice = "Move toward calmer waters. Leaving the storm is progress, not defeat."
        ),
        "Seven of Swords" to CardMeaning(
            upright = "Deception, strategy, cunning, acting alone.",
            advice = "Watch for dishonesty — yours or another's. Choose the straight path."
        ),
        "Eight of Swords" to CardMeaning(
            upright = "Restriction, self-imposed limits, feeling trapped.",
            advice = "The bindings are looser than they seem. You can free yourself — look."
        ),
        "Nine of Swords" to CardMeaning(
            upright = "Anxiety, worry, fear, sleepless nights.",
            advice = "Name the fear. Most of this dread lives in the mind — challenge it by daylight."
        ),
        "Ten of Swords" to CardMeaning(
            upright = "Painful ending, rock bottom, closure, release.",
            advice = "It's over — and that's the relief. The worst has passed; dawn follows."
        ),
        "Page of Swords" to CardMeaning(
            upright = "Curiosity, new ideas, mental energy, vigilance.",
            advice = "Ask questions. Your sharp curiosity uncovers what others overlook."
        ),
        "Knight of Swords" to CardMeaning(
            upright = "Ambition, drive, fast thinking, charging ahead.",
            advice = "Act decisively — but temper haste with strategy. Aim before you strike."
        ),
        "Queen of Swords" to CardMeaning(
            upright = "Clear-mindedness, honesty, independent judgment.",
            advice = "Think for yourself. Set clear boundaries and speak the honest truth."
        ),
        "King of Swords" to CardMeaning(
            upright = "Intellectual authority, truth, logic, ethical clarity.",
            advice = "Lead with reason and integrity. Let fair, clear judgment guide you."
        ),

        // Minor Arcana — Pentacles (earth: work, money, body, material world)
        "Ace of Pentacles" to CardMeaning(
            upright = "New opportunity, prosperity, manifestation, security.",
            advice = "Plant the seed. A tangible opportunity for growth is being offered."
        ),
        "Two of Pentacles" to CardMeaning(
            upright = "Balance, adaptability, time management, juggling.",
            advice = "Stay flexible. You can juggle it all if you keep light on your feet."
        ),
        "Three of Pentacles" to CardMeaning(
            upright = "Teamwork, collaboration, skill, building together.",
            advice = "Work with others. Your craft grows when combined with shared effort."
        ),
        "Four of Pentacles" to CardMeaning(
            upright = "Security, control, saving, holding on tightly.",
            advice = "Loosen your grip. Stability is good; clinging too hard blocks the flow."
        ),
        "Five of Pentacles" to CardMeaning(
            upright = "Hardship, insecurity, worry, feeling left out.",
            advice = "Ask for help. Support is nearby — don't suffer the cold alone."
        ),
        "Six of Pentacles" to CardMeaning(
            upright = "Generosity, giving and receiving, sharing wealth.",
            advice = "Balance the scales. Give freely, and accept help with grace."
        ),
        "Seven of Pentacles" to CardMeaning(
            upright = "Patience, long-term view, investment, assessment.",
            advice = "Let it grow. Your efforts need time — assess, then keep tending."
        ),
        "Eight of Pentacles" to CardMeaning(
            upright = "Diligence, mastery, skill development, dedication.",
            advice = "Refine your craft. Steady, focused practice builds real mastery."
        ),
        "Nine of Pentacles" to CardMeaning(
            upright = "Abundance, self-sufficiency, luxury, independence.",
            advice = "Enjoy your rewards. You earned this comfort through your own work."
        ),
        "Ten of Pentacles" to CardMeaning(
            upright = "Wealth, legacy, family, lasting foundations.",
            advice = "Build for the long term. Your work creates security for years to come."
        ),
        "Page of Pentacles" to CardMeaning(
            upright = "Ambition, study, a new venture, practical dreams.",
            advice = "Start learning. A grounded new goal rewards patient, steady study."
        ),
        "Knight of Pentacles" to CardMeaning(
            upright = "Reliability, hard work, routine, steady progress.",
            advice = "Be consistent. Unglamorous, dependable effort gets you there."
        ),
        "Queen of Pentacles" to CardMeaning(
            upright = "Nurturing, practicality, abundance, grounded security.",
            advice = "Tend to the practical. Care for your resources, home, and well-being."
        ),
        "King of Pentacles" to CardMeaning(
            upright = "Wealth, business acumen, leadership, abundant security.",
            advice = "Build lasting prosperity. Steward your success with grounded wisdom."
        )
    )

    private val defaultMeaning = CardMeaning(
        upright = "Energy, movement, action, and life lessons.",
        advice = "Stay open to the subtle flows of energy and act with intention."
    )

    fun getMeaning(cardName: String): CardMeaning {
        return meanings[cardName] ?: defaultMeaning
    }

    fun generateInterpretation(type: String, cards: List<String>): String {
        val intro = "🔮 *The cards have spoken.*\n\n"
        
        return when (type) {
            "Daily" -> {
                val card = cards.firstOrNull() ?: "The Fool"
                val meaning = getMeaning(card)
                "$intro*Daily Card: $card*\n\n" +
                "✨ *The Energy of the Day:*\n${meaning.upright}\n\n" +
                "🧘 *Wisdom & Advice:*\n${meaning.advice}\n\n" +
                "Use this guidance to ground yourself, breathe deeply, and align with the universe's natural rhythm."
            }
            else -> { // "3-Card"
                val past = cards.getOrNull(0) ?: "The Fool"
                val present = cards.getOrNull(1) ?: "The Magician"
                val future = cards.getOrNull(2) ?: "The High Priestess"
                
                val mPast = getMeaning(past)
                val mPresent = getMeaning(present)
                val mFuture = getMeaning(future)
                
                "$intro*Three-Card Spread Reading*\n\n" +
                "🕰️ *1. PAST — $past*\n" +
                "• Energy: ${mPast.upright}\n" +
                "• Foundation: This is the background of your current path.\n\n" +
                "🌟 *2. PRESENT — $present*\n" +
                "• Energy: ${mPresent.upright}\n" +
                "• Focus: ${mPresent.advice}\n\n" +
                "🌅 *3. FUTURE — $future*\n" +
                "• Potential: ${mFuture.upright}\n" +
                "• Guidance: Keep this energy in mind as you step forward."
            }
        }
    }

    data class CardMeaning(
        val upright: String,
        val advice: String
    )
}
