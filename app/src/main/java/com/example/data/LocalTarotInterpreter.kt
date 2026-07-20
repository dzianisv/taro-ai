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
