package com.example.data

object LocalTarotInterpreter {
    private val meanings = mapOf(
        // ---------------- Major Arcana ----------------
        "The Fool" to CardMeaning(
            keywords = listOf("new beginnings", "spontaneity", "innocence", "leap of faith"),
            upright = "New beginnings, optimism, trust in life's journey, spontaneity.",
            uprightAdvice = "Embrace the unknown. Take a leap of faith, but stay mindful of your surroundings.",
            reversed = "Recklessness, hesitation, or fear of the first step; you ignore real risks or refuse to grow.",
            reversedAdvice = "Look before you leap. Weigh the risks, then move — but don't freeze in fear."
        ),
        "The Magician" to CardMeaning(
            keywords = listOf("manifestation", "willpower", "resourcefulness", "inspired action"),
            upright = "Manifestation, resourcefulness, power, inspired action.",
            uprightAdvice = "You have all the tools you need to succeed. Channel your focus and manifest your goals.",
            reversed = "Untapped talent, manipulation, or scattered energy; your skills feel blocked or are used to deceive.",
            reversedAdvice = "Realign your intentions with integrity. Focus your gifts before they scatter or mislead."
        ),
        "The High Priestess" to CardMeaning(
            keywords = listOf("intuition", "mystery", "subconscious", "inner voice"),
            upright = "Intuition, sacred knowledge, divine feminine, the subconscious mind.",
            uprightAdvice = "Look within. Your intuition is whispering to you; trust your dreams and gut feelings.",
            reversed = "Ignored intuition, secrets kept from you, or disconnection from your inner voice as surface noise drowns truth.",
            reversedAdvice = "Get quiet and listen again. Stop overriding the quiet knowing you keep dismissing."
        ),
        "The Empress" to CardMeaning(
            keywords = listOf("abundance", "nurturing", "fertility", "creativity"),
            upright = "Abundance, creativity, nature, nurturing, fertility.",
            uprightAdvice = "Connect with the earth and your senses. Nurture your creative ideas and let them flourish.",
            reversed = "Creative block, self-neglect, or smothering others; abundance stalls and nurturing turns to dependence.",
            reversedAdvice = "Pour some care back into yourself first. A depleted well can nourish no one."
        ),
        "The Emperor" to CardMeaning(
            keywords = listOf("authority", "structure", "stability", "leadership"),
            upright = "Authority, structure, solid foundations, protective power.",
            uprightAdvice = "Establish order and boundaries. Act with confidence and bring structure to your chaos.",
            reversed = "Control turned rigid or tyrannical, or a lack of discipline; structure becomes a cage or crumbles entirely.",
            reversedAdvice = "Loosen your grip where you're forcing control. Lead with fairness, not domination."
        ),
        "The Hierophant" to CardMeaning(
            keywords = listOf("tradition", "guidance", "spiritual wisdom", "belonging"),
            upright = "Tradition, spiritual wisdom, conformity, seeking guidance.",
            uprightAdvice = "Honor tried-and-true wisdom. This is a time for learning and connecting with legacy systems.",
            reversed = "Rebellion, breaking convention, or questioning inherited beliefs; the old rules no longer fit who you are.",
            reversedAdvice = "Trust your own path over borrowed doctrine. Keep what serves you, release the rest."
        ),
        "The Lovers" to CardMeaning(
            keywords = listOf("love", "harmony", "choices", "values alignment"),
            upright = "Harmony, relationships, choices, alignment of values.",
            uprightAdvice = "Focus on alignment and balance in relationships. Choose paths that match your core values.",
            reversed = "Disharmony, misalignment, or a hard choice avoided; values clash and a relationship falls out of balance.",
            reversedAdvice = "Name what you truly want. Honest alignment beats a comfortable, dishonest compromise."
        ),
        "The Chariot" to CardMeaning(
            keywords = listOf("willpower", "determination", "victory", "control"),
            upright = "Willpower, determination, victory, overcoming obstacles.",
            uprightAdvice = "Stay disciplined and focused. Direct your opposing forces toward a singular, victorious goal.",
            reversed = "Lost direction, scattered drive, or aggression without control; the reins slip and momentum stalls.",
            reversedAdvice = "Regain focus before charging ahead. Steer with purpose, not force alone."
        ),
        "Strength" to CardMeaning(
            keywords = listOf("courage", "inner strength", "compassion", "patience"),
            upright = "Inner fortitude, courage, patience, compassion.",
            uprightAdvice = "Influence situations through gentle influence and patience rather than brute force.",
            reversed = "Self-doubt, raw emotion, or forced control; inner fear undermines your quiet power and patience thins.",
            reversedAdvice = "Be gentle with yourself. Tame the inner critic before trying to tame anything else."
        ),
        "The Hermit" to CardMeaning(
            keywords = listOf("introspection", "solitude", "inner guidance", "soul-searching"),
            upright = "Soul-searching, introspection, inner guidance, solitude.",
            uprightAdvice = "Take a step back. Spend some quiet time in contemplation to find the answers you seek.",
            reversed = "Isolation, loneliness, or withdrawal taken too far; solitude has curdled into avoidance of the world.",
            reversedAdvice = "Reach back out when you're ready. Reflection is medicine, but isolation is not."
        ),
        "Wheel of Fortune" to CardMeaning(
            keywords = listOf("cycles", "destiny", "turning point", "luck"),
            upright = "Destiny, turning point, luck, inevitable change.",
            uprightAdvice = "Accept that life is full of cycles. Adapt gracefully to the shifts occurring around you.",
            reversed = "Bad luck, resistance to change, or a cycle you keep repeating; the wheel feels stuck against you.",
            reversedAdvice = "Stop fighting the turn. Find the lesson in the loop so it doesn't circle back."
        ),
        "Justice" to CardMeaning(
            keywords = listOf("fairness", "truth", "accountability", "cause and effect"),
            upright = "Fairness, truth, cause and effect, accountability.",
            uprightAdvice = "Seek truth and act with integrity. Trust that the scales of karma will eventually balance.",
            reversed = "Unfairness, dishonesty, or avoided accountability; consequences are dodged and the truth stays hidden.",
            reversedAdvice = "Own your part honestly. Facing the truth now costs less than avoiding it later."
        ),
        "The Hanged Man" to CardMeaning(
            keywords = listOf("surrender", "new perspective", "pause", "letting go"),
            upright = "Surrender, letting go, new perspectives, pause.",
            uprightAdvice = "Do not force action. View your situation from a different angle and embrace the pause.",
            reversed = "Stalling, resistance, or needless martyrdom; you cling to control and refuse the shift in perspective.",
            reversedAdvice = "Let go of what you can't control. The delay ends when you stop resisting it."
        ),
        "Death" to CardMeaning(
            keywords = listOf("endings", "transformation", "transition", "rebirth"),
            upright = "Endings, transformation, transition, letting go.",
            uprightAdvice = "Do not fear the end. Clear away the old to make room for powerful new beginnings.",
            reversed = "Resisting a necessary ending, clinging to the past, or stalled transformation; you fear the change you need most.",
            reversedAdvice = "Release what's already over. Holding a closed chapter shut only delays your rebirth."
        ),
        "Temperance" to CardMeaning(
            keywords = listOf("balance", "moderation", "patience", "harmony"),
            upright = "Balance, moderation, patience, purpose.",
            uprightAdvice = "Seek the middle path. Combine different areas of your life in harmonious moderation.",
            reversed = "Excess, imbalance, or impatience; extremes pull you off-center and harmony feels impossible to find.",
            reversedAdvice = "Recalibrate slowly. Small, steady adjustments restore the balance that force cannot."
        ),
        "The Devil" to CardMeaning(
            keywords = listOf("temptation", "attachment", "addiction", "shadow self"),
            upright = "Attachment, restriction, shadow self, temptation.",
            uprightAdvice = "Recognize where you feel bound. You hold the key to free yourself from unhealthy habits.",
            reversed = "Breaking free, confronting addiction, or releasing what enslaves you; the chains loosen as awareness dawns.",
            reversedAdvice = "Name the habit that binds you and take one real step toward freedom today."
        ),
        "The Tower" to CardMeaning(
            keywords = listOf("upheaval", "sudden change", "revelation", "awakening"),
            upright = "Sudden upheaval, revelation, breakthrough, awakening.",
            uprightAdvice = "Allow old structures to crumble. It paves the way for building on a firmer foundation.",
            reversed = "Avoiding an inevitable collapse, internal upheaval, or fear of change; you brace against a fall you can't prevent.",
            reversedAdvice = "Stop propping up what's already broken. A controlled release hurts less than the crash."
        ),
        "The Star" to CardMeaning(
            keywords = listOf("hope", "renewal", "faith", "healing"),
            upright = "Hope, faith, renewal, serenity, spiritual glow.",
            uprightAdvice = "Have faith in the universe. Your future is bright, and healing energies are flowing to you.",
            reversed = "Lost hope, discouragement, or disconnection from faith; the light feels distant and inspiration runs dry.",
            reversedAdvice = "Tend a small flame of hope. Renewal returns gently, not all at once."
        ),
        "The Moon" to CardMeaning(
            keywords = listOf("illusion", "intuition", "fear", "subconscious"),
            upright = "Illusion, fear, anxiety, subconscious depths.",
            uprightAdvice = "Navigate by your inner light. Things may not be as they seem; let illusions dissolve.",
            reversed = "Confusion clearing, hidden truths surfacing, or fears released; the fog begins to lift and clarity returns.",
            reversedAdvice = "Trust the truth emerging now. Face the fear you've been avoiding in the dark."
        ),
        "The Sun" to CardMeaning(
            keywords = listOf("joy", "success", "vitality", "positivity"),
            upright = "Vitality, joy, success, warmth, radiant truth.",
            uprightAdvice = "Celebrate your life! Radiate positivity, clarity, and share your warmth with others.",
            reversed = "Temporary gloom, dimmed optimism, or delayed success; the joy is there but clouded over for now.",
            reversedAdvice = "Look for the small bright spots. The sun hasn't vanished, only slipped behind clouds."
        ),
        "Judgement" to CardMeaning(
            keywords = listOf("rebirth", "reckoning", "awakening", "calling"),
            upright = "Rebirth, absolution, calling, decision-making.",
            uprightAdvice = "Listen to your higher calling. It is time for self-evaluation and stepping into your power.",
            reversed = "Self-doubt, an ignored calling, or harsh self-judgment; you avoid the reckoning that would set you free.",
            reversedAdvice = "Forgive yourself and answer the call. Stop rehearsing old regrets on a loop."
        ),
        "The World" to CardMeaning(
            keywords = listOf("completion", "fulfillment", "wholeness", "accomplishment"),
            upright = "Completion, integration, travel, ultimate fulfillment.",
            uprightAdvice = "Celebrate your success. You have completed a major cycle and achieved wholeness.",
            reversed = "Unfinished business, delayed closure, or seeking completion elsewhere; the final step stays just out of reach.",
            reversedAdvice = "Tie up the loose end you keep avoiding. Completion is closer than it feels."
        ),

        // ---------------- Minor Arcana — Wands ----------------
        "Ace of Wands" to CardMeaning(
            keywords = listOf("inspiration", "new passion", "creative spark", "potential"),
            upright = "A spark of inspiration and creative potential; new passionate beginnings ready to ignite.",
            uprightAdvice = "Act on that flash of inspiration now, while the creative fire is bright.",
            reversed = "Delays, false starts, or a creative spark that fizzles; motivation stalls before it catches.",
            reversedAdvice = "Reconnect with what excites you. Clear the block before forcing the flame."
        ),
        "Two of Wands" to CardMeaning(
            keywords = listOf("planning", "future vision", "decisions", "personal power"),
            upright = "Planning ahead, weighing your options, and stepping into a wider world of possibility.",
            uprightAdvice = "Map your next move boldly. The world is bigger than your comfort zone.",
            reversed = "Fear of the unknown, poor planning, or playing it safe; you hesitate at the edge of a bigger life.",
            reversedAdvice = "Commit to one direction. Endless planning without a step leads nowhere."
        ),
        "Three of Wands" to CardMeaning(
            keywords = listOf("expansion", "foresight", "progress", "opportunity"),
            upright = "Expansion and foresight; momentum builds as early efforts begin to show real progress.",
            uprightAdvice = "Trust the groundwork you've laid. Watch the horizon for arriving opportunities.",
            reversed = "Delays, obstacles, or a lack of foresight; expansion stalls and plans meet unexpected resistance.",
            reversedAdvice = "Be patient — refine the plan while progress catches up to your vision."
        ),
        "Four of Wands" to CardMeaning(
            keywords = listOf("celebration", "home", "community", "milestones"),
            upright = "Celebration and harmony; joyful milestones shared with community, home, or a loving reunion.",
            uprightAdvice = "Pause to celebrate how far you've come, surrounded by people you love.",
            reversed = "Tension at home, a delayed celebration, or feeling you don't belong; the harmony feels strained.",
            reversedAdvice = "Rebuild your sense of home from within before seeking it in others."
        ),
        "Five of Wands" to CardMeaning(
            keywords = listOf("conflict", "competition", "tension", "disagreement"),
            upright = "Conflict and clashing egos; friction and competition that can sharpen you or simply exhaust you.",
            uprightAdvice = "Channel the tension into healthy competition, not pointless quarrels.",
            reversed = "Avoiding conflict, inner tension, or a truce reached; the fighting quiets but resentment may linger.",
            reversedAdvice = "Address the real issue directly. Suppressed conflict just resurfaces louder later."
        ),
        "Six of Wands" to CardMeaning(
            keywords = listOf("victory", "recognition", "success", "confidence"),
            upright = "Public victory and recognition; well-earned praise after hard-won effort and perseverance.",
            uprightAdvice = "Accept the acclaim gracefully — you earned this moment in the spotlight.",
            reversed = "Lack of recognition, self-doubt, or a fall from grace; the win feels hollow or delayed.",
            reversedAdvice = "Validate yourself first. Your worth doesn't depend on the crowd's applause."
        ),
        "Seven of Wands" to CardMeaning(
            keywords = listOf("defense", "perseverance", "standing your ground", "challenge"),
            upright = "Standing your ground and defending your position, holding firm against mounting challenges.",
            uprightAdvice = "Hold the line on what matters. You have the higher ground — defend it.",
            reversed = "Overwhelm, giving up, or feeling outnumbered; the constant defending has left you exhausted.",
            reversedAdvice = "Pick your battles. Not every challenge deserves your last reserve of energy."
        ),
        "Eight of Wands" to CardMeaning(
            keywords = listOf("momentum", "swift action", "movement", "news"),
            upright = "Rapid movement and swift progress; news or events arriving fast after a period of waiting.",
            uprightAdvice = "Move quickly while momentum favors you — hesitation lets the moment pass.",
            reversed = "Delays, frustration, or scattered energy; progress stalls and messages get crossed or lost.",
            reversedAdvice = "Slow down and untangle the chaos before launching your next move."
        ),
        "Nine of Wands" to CardMeaning(
            keywords = listOf("resilience", "persistence", "boundaries", "last stand"),
            upright = "Resilience and grit; wounded but still standing, guarding what you've fought hard to keep.",
            uprightAdvice = "You're closer than you think. Summon one last push before you quit.",
            reversed = "Exhaustion, defensiveness, or paranoia; your guard is up so high it keeps everyone out.",
            reversedAdvice = "Rest and lower your defenses. Not every approach is an attack."
        ),
        "Ten of Wands" to CardMeaning(
            keywords = listOf("burden", "responsibility", "overload", "duty"),
            upright = "Carrying a heavy burden, overwhelmed by responsibilities you've taken on all at once.",
            uprightAdvice = "Set some of the load down. You don't have to carry it all alone.",
            reversed = "Releasing burdens, delegating, or collapse under the weight; the load finally becomes unbearable.",
            reversedAdvice = "Delegate or let go. Martyrdom serves no one, least of all you."
        ),
        "Page of Wands" to CardMeaning(
            keywords = listOf("enthusiasm", "exploration", "free spirit", "discovery"),
            upright = "An enthusiastic free spirit full of curiosity, ready to explore ideas and chase adventure.",
            uprightAdvice = "Say yes to the new adventure calling you — follow your curiosity.",
            reversed = "Restlessness, procrastination, or unformed ideas; enthusiasm scatters before it turns into action.",
            reversedAdvice = "Ground your excitement in one real first step, not a dozen daydreams."
        ),
        "Knight of Wands" to CardMeaning(
            keywords = listOf("passion", "adventure", "impulsiveness", "energy"),
            upright = "A passionate, adventurous charge forward; bold energy that acts on desire without hesitation.",
            uprightAdvice = "Channel your fire toward a clear goal before it burns in every direction.",
            reversed = "Recklessness, impatience, or burnout; passion becomes haste and bold plans fall apart.",
            reversedAdvice = "Temper your impulses. Passion lasts longer when it isn't spent all at once."
        ),
        "Queen of Wands" to CardMeaning(
            keywords = listOf("confidence", "warmth", "charisma", "independence"),
            upright = "Confident, warm, and magnetic; a charismatic presence that draws others in effortlessly.",
            uprightAdvice = "Own your radiance. Lead with warmth and let your confidence light the room.",
            reversed = "Insecurity, jealousy, or self-doubt hidden behind bravado; your inner fire flickers low.",
            reversedAdvice = "Reconnect with your self-worth. Your light doesn't dim because someone else shines."
        ),
        "King of Wands" to CardMeaning(
            keywords = listOf("leadership", "vision", "boldness", "charisma"),
            upright = "A visionary leader; bold, charismatic, and able to inspire others toward a big vision.",
            uprightAdvice = "Lead with vision and courage. Others will follow your conviction.",
            reversed = "Impulsive leadership, arrogance, or a domineering ego; vision turns to bluster or tyranny.",
            reversedAdvice = "Listen before you command. True authority uplifts rather than overpowers."
        ),

        // ---------------- Minor Arcana — Cups ----------------
        "Ace of Cups" to CardMeaning(
            keywords = listOf("new love", "emotional beginnings", "compassion", "intuition"),
            upright = "An overflowing heart; new love, deep emotional connection, and a fresh wellspring of compassion.",
            uprightAdvice = "Open your heart fully. Let love and creativity pour in without guarding it.",
            reversed = "Blocked emotions, emptiness, or love held back; the heart's cup feels closed or drained.",
            reversedAdvice = "Tend your emotional wounds. You can't pour from a heart you keep sealed shut."
        ),
        "Two of Cups" to CardMeaning(
            keywords = listOf("partnership", "attraction", "connection", "mutual respect"),
            upright = "A powerful mutual attraction and partnership; two souls meeting as equals in genuine connection.",
            uprightAdvice = "Nurture this connection with honesty. True partnership is built on mutual respect.",
            reversed = "Imbalance, miscommunication, or a one-sided connection; the harmony between two people falters.",
            reversedAdvice = "Restore balance through honest conversation before resentment takes root."
        ),
        "Three of Cups" to CardMeaning(
            keywords = listOf("friendship", "celebration", "community", "joy"),
            upright = "Joyful celebration with friends; community, belonging, and shared happiness among close companions.",
            uprightAdvice = "Gather your people and celebrate. Joy multiplies when it's shared.",
            reversed = "Gossip, isolation, or overindulgence; the celebration sours or friendships feel strained.",
            reversedAdvice = "Choose your circle carefully. Real friends lift you rather than drain you."
        ),
        "Four of Cups" to CardMeaning(
            keywords = listOf("apathy", "contemplation", "boredom", "reevaluation"),
            upright = "Apathy and discontent; so absorbed in what's missing that you overlook a gift being offered.",
            uprightAdvice = "Look up from your discontent. An opportunity is right in front of you.",
            reversed = "Renewed interest, acceptance, or emerging from withdrawal; motivation and gratitude begin to return.",
            reversedAdvice = "Say yes to what's offered now. The dull spell is finally lifting."
        ),
        "Five of Cups" to CardMeaning(
            keywords = listOf("grief", "loss", "regret", "disappointment"),
            upright = "Grief and regret over loss, so fixed on what spilled that you miss what still remains.",
            uprightAdvice = "Grieve honestly, then turn around — two cups still stand behind you.",
            reversed = "Acceptance, healing, or moving on; you begin to forgive and recover what wasn't lost.",
            reversedAdvice = "Release the regret you keep replaying. Forgiveness frees you, not the past."
        ),
        "Six of Cups" to CardMeaning(
            keywords = listOf("nostalgia", "memories", "innocence", "reunion"),
            upright = "Nostalgia and sweet memories; comfort from the past, innocence, and warm reunions.",
            uprightAdvice = "Draw comfort from happy memories, but don't live entirely in them.",
            reversed = "Stuck in the past, idealizing what's gone, or unresolved childhood wounds surfacing now.",
            reversedAdvice = "Honor the past, then step firmly into the present it prepared you for."
        ),
        "Seven of Cups" to CardMeaning(
            keywords = listOf("choices", "illusion", "fantasy", "wishful thinking"),
            upright = "Many tempting options and fantasies; imagination running wild among illusions and wishful thinking.",
            uprightAdvice = "Ground your dreams in reality. Choose one real path over many mirages.",
            reversed = "Clarity returning, decisiveness, or overwhelm fading; the fog of too many choices finally clears.",
            reversedAdvice = "Commit to what's real and achievable. Fantasy is comfortable but leads nowhere."
        ),
        "Eight of Cups" to CardMeaning(
            keywords = listOf("walking away", "seeking meaning", "departure", "transition"),
            upright = "Walking away from what no longer fulfills you in search of deeper meaning and truth.",
            uprightAdvice = "Have the courage to leave what's empty, even when it looks fine outside.",
            reversed = "Fear of leaving, aimless drifting, or returning to what you outgrew; you stall at the threshold.",
            reversedAdvice = "Get honest about why you're staying. Comfort isn't the same as fulfillment."
        ),
        "Nine of Cups" to CardMeaning(
            keywords = listOf("contentment", "satisfaction", "wishes", "gratitude"),
            upright = "Emotional satisfaction and contentment; wishes fulfilled and deep gratitude — the classic 'wish card'.",
            uprightAdvice = "Savor this contentment and give thanks. Your wish is within reach.",
            reversed = "Dissatisfaction, smugness, or unfulfilled wishes; outward abundance hides an inner emptiness.",
            reversedAdvice = "Look beneath the surface — real fulfillment isn't measured by what you accumulate."
        ),
        "Ten of Cups" to CardMeaning(
            keywords = listOf("harmony", "family", "lasting happiness", "fulfillment"),
            upright = "Lasting emotional harmony; a loving family, deep bonds, and a picture of contentment fulfilled.",
            uprightAdvice = "Cherish the harmony around you and invest in the bonds that sustain it.",
            reversed = "Broken harmony, family strife, or a mismatch between the dream and reality of home.",
            reversedAdvice = "Mend the relationships that matter honestly, not by chasing a perfect image."
        ),
        "Page of Cups" to CardMeaning(
            keywords = listOf("creativity", "intuition", "new feelings", "sensitivity"),
            upright = "A gentle, imaginative messenger; new feelings, creative intuition, and openhearted sensitivity emerging.",
            uprightAdvice = "Follow that creative nudge or tender feeling with childlike openness.",
            reversed = "Emotional immaturity, escapism, or blocked creativity; feelings turn moody or overly guarded.",
            reversedAdvice = "Let yourself feel without hiding. Emotional honesty isn't weakness."
        ),
        "Knight of Cups" to CardMeaning(
            keywords = listOf("romance", "charm", "idealism", "following the heart"),
            upright = "A romantic idealist following the heart; charm, beauty, and emotion-led pursuit of a dream.",
            uprightAdvice = "Follow your heart, but keep one foot planted in reality.",
            reversed = "Moodiness, unrealistic ideals, or empty charm; romantic promises outrun genuine commitment.",
            reversedAdvice = "Match your grand feelings with grounded action, or they stay just words."
        ),
        "Queen of Cups" to CardMeaning(
            keywords = listOf("compassion", "empathy", "emotional depth", "intuition"),
            upright = "Deep compassion and emotional wisdom; a nurturing intuitive who feels others' hearts with grace.",
            uprightAdvice = "Trust your intuition and lead with empathy — your sensitivity is a gift.",
            reversed = "Emotional overwhelm, martyrdom, or lost boundaries; you absorb others' feelings until you drown.",
            reversedAdvice = "Protect your own energy. You can care deeply without carrying everyone's pain."
        ),
        "King of Cups" to CardMeaning(
            keywords = listOf("emotional balance", "compassion", "diplomacy", "composure"),
            upright = "Mastery of emotion; a calm, compassionate presence balancing deep feeling with steady composure.",
            uprightAdvice = "Lead from a place of calm compassion. Feel deeply, but stay centered.",
            reversed = "Emotional manipulation, moodiness, or suppressed feelings; the calm surface hides turbulent depths.",
            reversedAdvice = "Let your feelings breathe honestly instead of bottling or weaponizing them."
        ),

        // ---------------- Minor Arcana — Swords ----------------
        "Ace of Swords" to CardMeaning(
            keywords = listOf("clarity", "truth", "breakthrough", "new idea"),
            upright = "A breakthrough of clarity and truth; a sharp new idea cutting cleanly through confusion.",
            uprightAdvice = "Speak your truth plainly. This moment of clarity is a blade — use it well.",
            reversed = "Confusion, clouded judgment, or misused words; the clarity dulls and truth gets tangled.",
            reversedAdvice = "Gather the full picture before deciding. Don't wield half-truths as weapons."
        ),
        "Two of Swords" to CardMeaning(
            keywords = listOf("indecision", "stalemate", "avoidance", "difficult choice"),
            upright = "A tense stalemate; blindfolded and guarding your heart, avoiding a choice you must eventually face.",
            uprightAdvice = "Remove the blindfold and weigh the facts. Avoiding the choice only prolongs the tension.",
            reversed = "Indecision breaking, information revealed, or emotional overwhelm; the stalemate finally shifts one way.",
            reversedAdvice = "Make the call now that the truth is clear — hesitation has run its course."
        ),
        "Three of Swords" to CardMeaning(
            keywords = listOf("heartbreak", "grief", "sorrow", "painful truth"),
            upright = "Heartbreak and painful truth; sorrow that pierces the heart but clears the air of illusion.",
            uprightAdvice = "Let yourself feel the hurt fully — grief moving through you is grief healing.",
            reversed = "Recovery, forgiveness, or lingering pain; the wound begins to close, though the ache still echoes.",
            reversedAdvice = "Release the stored hurt gently. Healing comes when you stop reopening the wound."
        ),
        "Four of Swords" to CardMeaning(
            keywords = listOf("rest", "recovery", "contemplation", "stillness"),
            upright = "A needed pause for rest and recovery; retreat, stillness, and quiet healing after strain.",
            uprightAdvice = "Give yourself permission to rest. Recovery is not laziness — it's repair.",
            reversed = "Burnout, restlessness, or forced re-entry; you resist the rest your body and mind demand.",
            reversedAdvice = "Stop pushing through exhaustion. Real rest now prevents collapse later."
        ),
        "Five of Swords" to CardMeaning(
            keywords = listOf("conflict", "defeat", "hollow victory", "tension"),
            upright = "Conflict and hollow victory; winning the argument but losing goodwill, ego over connection.",
            uprightAdvice = "Ask if being right is worth the cost. Some wins aren't worth winning.",
            reversed = "Reconciliation, releasing resentment, or ongoing tension; a chance to make amends or walk away for good.",
            reversedAdvice = "Choose peace over pride. Lay down the grudge you've carried too long."
        ),
        "Six of Swords" to CardMeaning(
            keywords = listOf("transition", "moving on", "recovery", "release"),
            upright = "A gradual move toward calmer waters; transition, recovery, and leaving turbulence behind.",
            uprightAdvice = "Trust this transition even if it's slow. Calmer waters are ahead.",
            reversed = "Resistance to change, unfinished baggage, or feeling stuck; you carry old troubles into new places.",
            reversedAdvice = "Unpack what you're dragging along before it weighs down your fresh start."
        ),
        "Seven of Swords" to CardMeaning(
            keywords = listOf("deception", "strategy", "stealth", "secrecy"),
            upright = "Cunning strategy or deception; acting alone, keeping secrets, or slipping away with something.",
            uprightAdvice = "Act with integrity. If a plan needs hiding, question whether it's honest.",
            reversed = "Confession, getting caught, or returning to honesty; secrets surface and the deception unravels.",
            reversedAdvice = "Come clean before the truth exposes you. Honesty costs less than getting caught."
        ),
        "Eight of Swords" to CardMeaning(
            keywords = listOf("restriction", "self-limitation", "fear", "feeling trapped"),
            upright = "Feeling trapped and powerless, though the bindings are largely of your own mind's making.",
            uprightAdvice = "Notice the exit you can't yet see — many of these walls aren't real.",
            reversed = "Freedom, a new perspective, or deeper entrapment; you either break the mental cage or sink further in.",
            reversedAdvice = "Challenge the story keeping you stuck. You have more power than fear admits."
        ),
        "Nine of Swords" to CardMeaning(
            keywords = listOf("anxiety", "worry", "nightmares", "dread"),
            upright = "Anxiety and sleepless dread; fears magnified in the dark, worry far heavier than reality warrants.",
            uprightAdvice = "Name your fears in daylight — most shrink once spoken aloud.",
            reversed = "Anxiety easing, hope returning, or despair deepening; the nightmare either releases or tightens its grip.",
            reversedAdvice = "Reach out for support. You don't have to face these fears alone at 3am."
        ),
        "Ten of Swords" to CardMeaning(
            keywords = listOf("painful ending", "rock bottom", "release", "closure"),
            upright = "A painful ending and rock bottom, but the worst is over — the only way now is up.",
            uprightAdvice = "Accept the ending fully. Hitting bottom means the climb back can begin.",
            reversed = "Slow recovery, resisting the inevitable, or a relapse; you rise gradually or cling to a finished chapter.",
            reversedAdvice = "Let the ending be final. Refusing to close it only prolongs the pain."
        ),
        "Page of Swords" to CardMeaning(
            keywords = listOf("curiosity", "new ideas", "vigilance", "truth-seeking"),
            upright = "A curious, sharp-minded messenger; hungry for truth, ideas, and mental adventure.",
            uprightAdvice = "Ask the bold questions. Your curiosity uncovers what others overlook.",
            reversed = "Gossip, scattered thoughts, or all talk; sharp words used carelessly or ideas that never land.",
            reversedAdvice = "Think before you speak. Sharpen your focus instead of spraying opinions."
        ),
        "Knight of Swords" to CardMeaning(
            keywords = listOf("ambition", "drive", "directness", "haste"),
            upright = "A fast, ambitious charge toward a goal; direct, driven, and unwilling to slow down.",
            uprightAdvice = "Use your drive to break through — but aim before you charge.",
            reversed = "Recklessness, aggression, or scattered haste; ambition outruns strategy and words cut too sharply.",
            reversedAdvice = "Slow your charge. Force without direction leaves damage in its wake."
        ),
        "Queen of Swords" to CardMeaning(
            keywords = listOf("independence", "clarity", "honesty", "boundaries"),
            upright = "Clear-eyed independence and honest perception; sharp wit and firm boundaries born of experience.",
            uprightAdvice = "Speak the truth directly and hold your boundaries — clarity is a kindness.",
            reversed = "Coldness, bitterness, or harsh judgment; wisdom curdles into cynicism and words wound.",
            reversedAdvice = "Soften your edge. Honesty lands better when it isn't sharpened into cruelty."
        ),
        "King of Swords" to CardMeaning(
            keywords = listOf("intellect", "authority", "truth", "clear thinking"),
            upright = "Intellectual authority and impartial judgment; leading with clear reason, ethics, and truth.",
            uprightAdvice = "Decide with logic and fairness. Let clear thinking guide your authority.",
            reversed = "Manipulation, coldness, or misused power; intellect twisted toward control or harsh judgment.",
            reversedAdvice = "Pair your logic with compassion. Truth without heart becomes a weapon."
        ),

        // ---------------- Minor Arcana — Pentacles ----------------
        "Ace of Pentacles" to CardMeaning(
            keywords = listOf("opportunity", "prosperity", "new venture", "abundance"),
            upright = "A tangible new opportunity; the seed of prosperity, security, and material abundance offered to you.",
            uprightAdvice = "Plant this opportunity with care — it can grow into lasting security.",
            reversed = "A missed chance, poor planning, or scarcity fears; the opportunity slips or stays merely potential.",
            reversedAdvice = "Don't let fear or delay waste a real opening. Ground your plan practically."
        ),
        "Two of Pentacles" to CardMeaning(
            keywords = listOf("balance", "adaptability", "juggling", "priorities"),
            upright = "Juggling multiple demands with agility; balancing time, money, and priorities as circumstances shift.",
            uprightAdvice = "Stay flexible and prioritize. You can keep the plates spinning with a lighter grip.",
            reversed = "Overwhelm, dropped balls, or disorganization; too many commitments tip the balance into chaos.",
            reversedAdvice = "Simplify and let something go. You can't juggle everything without dropping what matters."
        ),
        "Three of Pentacles" to CardMeaning(
            keywords = listOf("teamwork", "collaboration", "skill", "craftsmanship"),
            upright = "Skilled collaboration; teamwork and craftsmanship coming together to build something of lasting value.",
            uprightAdvice = "Value the team around you. Great work is rarely built alone.",
            reversed = "Poor teamwork, misaligned goals, or unrecognized effort; collaboration breaks down and quality suffers.",
            reversedAdvice = "Clarify roles and speak up. A team only works when everyone's heard."
        ),
        "Four of Pentacles" to CardMeaning(
            keywords = listOf("security", "control", "saving", "holding on"),
            upright = "Holding tightly to security and possessions; stability guarded, but a grip so firm it borders on fear.",
            uprightAdvice = "Protect what's yours, but don't let holding on close your open hands.",
            reversed = "Greed loosening, financial insecurity, or letting go; the tight grip either releases or clenches tighter.",
            reversedAdvice = "Loosen your hold. Generosity and flow bring more than hoarding ever will."
        ),
        "Five of Pentacles" to CardMeaning(
            keywords = listOf("hardship", "scarcity", "insecurity", "isolation"),
            upright = "Material hardship and feeling left out in the cold; scarcity, worry, and isolation in tough times.",
            uprightAdvice = "Ask for help — support is nearer than your hardship lets you see.",
            reversed = "Recovery, renewed hope, or lingering struggle; you climb out of hard times or the isolation deepens.",
            reversedAdvice = "Accept the help being offered. Recovery starts when you stop suffering alone."
        ),
        "Six of Pentacles" to CardMeaning(
            keywords = listOf("generosity", "giving and receiving", "charity", "balance"),
            upright = "Balanced generosity; giving and receiving in fair measure, sharing wealth or support with grace.",
            uprightAdvice = "Give freely where you can, and receive graciously when it's your turn.",
            reversed = "Strings-attached giving, debt, or power imbalance; generosity turns to control or one-sided dependence.",
            reversedAdvice = "Watch for uneven exchanges. Healthy giving doesn't come with hidden strings."
        ),
        "Seven of Pentacles" to CardMeaning(
            keywords = listOf("patience", "long-term view", "investment", "assessment"),
            upright = "Pausing to assess a long-term investment; patience as effort slowly ripens into future reward.",
            uprightAdvice = "Trust the slow growth. What you've planted needs time, not constant digging up.",
            reversed = "Impatience, wasted effort, or poor payoff; you doubt the investment or pull out too soon.",
            reversedAdvice = "Reassess honestly, then commit or pivot — don't abandon a harvest half-grown."
        ),
        "Eight of Pentacles" to CardMeaning(
            keywords = listOf("diligence", "mastery", "skill-building", "dedication"),
            upright = "Diligent focus and craftsmanship; refining your skills through patient, dedicated repetition.",
            uprightAdvice = "Keep honing your craft. Mastery is built one careful detail at a time.",
            reversed = "Perfectionism, cut corners, or lost motivation; effort becomes drudgery or quality slips.",
            reversedAdvice = "Reconnect with why the work matters, or realign your effort toward it."
        ),
        "Nine of Pentacles" to CardMeaning(
            keywords = listOf("abundance", "self-sufficiency", "independence", "reward"),
            upright = "Earned abundance and refined independence; enjoying the fruits of your own disciplined labor.",
            uprightAdvice = "Savor the rewards you built yourself — self-sufficiency is its own luxury.",
            reversed = "Financial dependence, overwork, or hollow success; the luxury feels unearned or bought at a cost.",
            reversedAdvice = "Rebuild genuine independence. Real security comes from within, not just your accounts."
        ),
        "Ten of Pentacles" to CardMeaning(
            keywords = listOf("legacy", "wealth", "family", "long-term security"),
            upright = "Lasting wealth and legacy; generational security, family foundations, and enduring material stability.",
            uprightAdvice = "Build for the long term and the people who'll inherit what you create.",
            reversed = "Financial instability, family disputes, or fleeting wealth; the foundation or legacy feels at risk.",
            reversedAdvice = "Tend the roots — lasting security needs strong relationships, not just assets."
        ),
        "Page of Pentacles" to CardMeaning(
            keywords = listOf("ambition", "study", "new opportunity", "manifestation"),
            upright = "A studious, ambitious beginner; grounding a dream into a practical plan or new venture.",
            uprightAdvice = "Turn your ambition into a concrete first step — study, plan, then act.",
            reversed = "Procrastination, lack of focus, or unrealistic plans; ambition stays a daydream without grounding.",
            reversedAdvice = "Stop stalling and commit to the boring first step. Progress beats perfection."
        ),
        "Knight of Pentacles" to CardMeaning(
            keywords = listOf("reliability", "hard work", "routine", "patience"),
            upright = "Steady, reliable, and methodical; the patient worker who finishes what they start, step by step.",
            uprightAdvice = "Keep going at your steady pace. Consistency will carry you to the finish.",
            reversed = "Stagnation, boredom, or stubbornness; reliability hardens into rigid routine and lost momentum.",
            reversedAdvice = "Introduce a little change. Routine is a tool, not a rut to disappear into."
        ),
        "Queen of Pentacles" to CardMeaning(
            keywords = listOf("nurturing", "practicality", "abundance", "groundedness"),
            upright = "A grounded, nurturing provider; balancing warmth, home, and work with practical, generous care.",
            uprightAdvice = "Care for yourself as generously as you care for everyone else.",
            reversed = "Self-neglect, work-life imbalance, or smothering; you give so much there's nothing left for you.",
            reversedAdvice = "Refill your own cup first. You can't nurture others from empty reserves."
        ),
        "King of Pentacles" to CardMeaning(
            keywords = listOf("wealth", "abundance", "security", "leadership"),
            upright = "Abundant, grounded mastery; a successful provider who's built lasting wealth and leads with generosity.",
            uprightAdvice = "Steward your success wisely and share the abundance you've worked to build.",
            reversed = "Greed, materialism, or controlling wealth; security becomes hoarding or status obsession.",
            reversedAdvice = "Measure your worth beyond money. Generosity, not control, is true wealth."
        )
    )

    private val defaultMeaning = CardMeaning(
        keywords = listOf("energy", "change", "reflection", "intuition"),
        upright = "A moment of shifting energy invites reflection; trust the quiet currents guiding your path forward.",
        uprightAdvice = "Stay open to subtle signs and act with calm, grounded intention.",
        reversed = "Blocked or scattered energy; a pause where momentum stalls and clarity feels just out of reach.",
        reversedAdvice = "Slow down, breathe, and let the confusion settle before making your next move."
    )

    fun getMeaning(cardName: String): CardMeaning {
        return meanings[cardName] ?: defaultMeaning
    }

    fun randomOrientation(): String = if (kotlin.random.Random.nextInt(100) < 75) "Upright" else "Reversed"

    fun meaningFor(cardName: String, orientation: String): Pair<String, String> {
        val m = getMeaning(cardName)
        return if (orientation.equals("Reversed", ignoreCase = true)) {
            m.reversed to m.reversedAdvice
        } else {
            m.upright to m.uprightAdvice
        }
    }

    fun generateInterpretation(type: String, cards: List<String>): String {
        val intro = "🔮 *The cards have spoken.*\n\n"

        return when (type) {
            "Daily" -> {
                val card = cards.firstOrNull() ?: "The Fool"
                val orientation = randomOrientation()
                val (meaningText, adviceText) = meaningFor(card, orientation)
                "$intro*Daily Card: $card ($orientation)*\n\n" +
                "✨ *The Energy of the Day:*\n$meaningText\n\n" +
                "🧘 *Wisdom & Advice:*\n$adviceText\n\n" +
                "Use this guidance to ground yourself, breathe deeply, and align with the universe's natural rhythm."
            }
            else -> { // "3-Card"
                val past = cards.getOrNull(0) ?: "The Fool"
                val present = cards.getOrNull(1) ?: "The Magician"
                val future = cards.getOrNull(2) ?: "The High Priestess"

                val pastOrientation = randomOrientation()
                val presentOrientation = randomOrientation()
                val futureOrientation = randomOrientation()

                val (pastMeaningText, _) = meaningFor(past, pastOrientation)
                val (presentMeaningText, presentAdviceText) = meaningFor(present, presentOrientation)
                val (futureMeaningText, futureAdviceText) = meaningFor(future, futureOrientation)

                "$intro*Three-Card Spread Reading*\n\n" +
                "🕰️ *1. PAST — $past ($pastOrientation)*\n" +
                "• Energy: $pastMeaningText\n" +
                "• Foundation: This is the background of your current path.\n\n" +
                "🌟 *2. PRESENT — $present ($presentOrientation)*\n" +
                "• Energy: $presentMeaningText\n" +
                "• Focus: $presentAdviceText\n\n" +
                "🌅 *3. FUTURE — $future ($futureOrientation)*\n" +
                "• Potential: $futureMeaningText\n" +
                "• Guidance: $futureAdviceText"
            }
        }
    }

    data class CardMeaning(
        val keywords: List<String>,
        val upright: String,
        val uprightAdvice: String,
        val reversed: String,
        val reversedAdvice: String
    )
}
