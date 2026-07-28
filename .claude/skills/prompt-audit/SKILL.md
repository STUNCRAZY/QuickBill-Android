---
name: prompt-audit
description: Reviews, audits, and rewrites prompts so Claude gets them right on the first pass instead of the third. Use this whenever the user asks to improve, tighten, fix, review, critique, or "re-engineer" a prompt; when a prompt produced the wrong result and they want to know why; when they're about to kick off a heavy reasoning task or a long coding session and want the instructions sharpened first; or when they paste a prompt and ask "will this work?" / "how would you phrase this?". Also use it proactively before dispatching a large, expensive, or hard-to-reverse task, since a two-minute prompt audit is far cheaper than a wasted twenty-minute run.
metadata:
  origin: QuickBill
---

# Prompt Audit

Diagnose why a prompt will underperform, then rewrite it. The target isn't a
*longer* prompt or a *prettier* one — it's the fewest words that reliably produce
the right result on the first attempt.

## What "efficient" means here

Two costs, and they trade against each other:

- **Wasted turns** — the model misreads the ask, delivers the wrong thing, and a
  correction round-trip is needed. Expensive: a full generation plus the user's
  attention.
- **Wasted tokens** — the prompt carries words that don't change what the model
  does. Cheap individually, but they dilute attention and bury the parts that
  matter.

Optimize for the first, then trim against the second. A prompt that is 40% longer
but eliminates a correction round-trip is a clear win. A prompt that is 40% longer
and changes nothing is pure loss. **Every clause you add should survive the
question "what would the model do differently without this?"** If the answer is
"nothing", cut it.

This is the most common failure mode of prompt improvement: the auditor pattern-
matches "good prompt = detailed prompt" and inflates a fine 12-word instruction
into a 300-word brief with headers. That is a worse prompt. Adding structure to a
task that never needed it costs the user tokens, costs them reading time, and
tells the model that trivial work deserves ceremony.

## Why structure helps at all

Understanding the mechanism keeps you from cargo-culting the format:

- **Generation is sequential and commits early.** The model resolves ambiguity as
  soon as it hits it, then stays consistent with that resolution. An unclear goal
  in the first sentence doesn't produce a hedged answer — it produces a confident
  answer to the wrong question. This is why the goal goes first: it's the frame
  everything after is interpreted through.
- **Attention is finite and unevenly spread.** Beginnings and ends get the most
  reliable weight. A hard constraint buried mid-paragraph in a wall of context is
  the single most common cause of "it ignored my instruction."
- **In agentic loops, every ambiguity becomes tool calls.** When scope is vague,
  the model explores to resolve it — greps, file reads, speculative branches.
  Naming the three files up front replaces ten discovery calls.
- **Single-turn work can't ask.** Anything underspecified gets resolved by
  guessing. Stating the assumption you want costs one line and removes the coin flip.

## The audit rubric

Work through these in order. Each is a question about the prompt, and each has a
characteristic failure it prevents.

**1. Goal — is the outcome stated before the detail?**
Failure: the model optimizes for the wrong objective and does it well.
Check: can you state the deliverable from the first two sentences alone?

**2. Concreteness — are the nouns real?**
Failure: exploratory thrashing, or confident work on the wrong target.
Check: paths, symbol names, versions, actual error text. Replace "the config" with
`core/data/build.gradle.kts`. Replace "it's throwing an error" with the stack trace.
Pasting the real output is almost always better than describing it.

**3. Scope — is the boundary drawn?**
Failure: scope creep (refactoring things nobody asked about), or scope collapse
(fixing one call site when twelve exist).
Check: is it clear what's in, and is at least one tempting-but-excluded thing named?
Negative space is cheap and high-yield: "don't touch the Room schema" prevents an
entire class of unwanted diff.

**4. Done-criteria — how does the model know it's finished?**
Failure: over-delivery (gold-plating) or under-delivery (stopping at the happy path).
Check: is there an observable stopping condition? "until `./gradlew test` passes"
beats "make it work."

**5. Output shape — is the form of the answer specified when it matters?**
Failure: a prose essay when a diff was wanted, or vice versa.
Check: only add this if the natural default would be wrong. Most of the time the
model's default shape is fine and specifying it is waste.

**6. Constraints and invariants — what must stay true?**
Failure: the model solves the stated problem by breaking something unstated.
Check: API compatibility, performance budgets, style rules, "don't add dependencies."

**7. Buried instructions — is anything critical stranded mid-prompt?**
Failure: silently dropped requirements.
Check: hard constraints belong at the top or the bottom, not in the middle of a
context dump. If the prompt has a long paste, the instructions go *after* it.

**8. Over-specification — is the method dictated where it shouldn't be?**
Failure: the model follows a worse path than it would have found itself.
Check: prescribing *how* is right when a specific approach is required (house
conventions, a known-good pattern). It's wrong when the user is guessing at
implementation and the model has better information from reading the code.

## Triage

Not every finding is worth acting on. Rank them:

- **Blocking** — the prompt will produce the wrong deliverable. Missing goal,
  contradictory requirements, ambiguity where two readings mean different work.
- **Costly** — the prompt will get there, but through wasted turns. Vague nouns,
  no done-criteria, no scope boundary.
- **Polish** — marginal. Mention briefly or skip.

If everything is polish, say so and hand the prompt back unchanged. "This is
already good" is a legitimate and useful audit result. Users learn more from a
clean bill of health on a good prompt than from invented nitpicks.

## Complexity changes what to add

The relationship is not "harder task, more instructions." It's that the *dominant
failure mode shifts*, and you counter whichever one is live.

### Tier 1 — Mechanical (rename, add a field, bump a version)

Dominant risk: none, really. The task is self-evident.
Add: nothing. Just name the thing precisely.

> Rename `InvoiceDao.findAll()` to `observeAll()` and update all call sites.

Adding a Context/Constraints/Success-Criteria scaffold here is actively harmful —
it's noise around a one-line ask.

### Tier 2 — Bounded implementation (a feature, a bug fix, a focused refactor)

Dominant risk: misread scope, wrong assumptions about existing conventions.
Add: goal, concrete anchors, scope boundary, done-criteria. Four short lines.

> Add offline caching to the invoice list.
> `feature/invoice/InvoiceListViewModel.kt` currently hits the repo directly.
> Cache in the existing Room DB — don't add a new persistence layer, and don't
> change the public ViewModel API. Done when the list renders from cache with
> airplane mode on and `./gradlew :feature:invoice:test` passes.

### Tier 3 — Heavy reasoning (architecture, tricky debugging, design tradeoffs)

Dominant risk: *confident wrong direction*. The model commits to a framing early
and reasons impeccably from a bad premise. Long output makes it expensive to discover.
Add: the symptom evidence rather than your diagnosis, what you've already ruled
out, the invariants, and a checkpoint before implementation.

> Invoices occasionally save with a null `customerId` — about 1 in 200, only in
> production. Stack trace and the failing row are below.
> Already ruled out: form validation (unit-tested), and the API contract (the
> payload has the field).
> Constraint: the fix can't require a schema migration this release.
> Give me the two or three most likely root causes with the evidence for each
> before writing any fix.

The last line is the highest-leverage sentence in the whole prompt. A cheap
checkpoint before a long generation converts a possible twenty-minute wrong turn
into a thirty-second correction. Note also what it *doesn't* do: it doesn't tell
the model what's wrong. Handing over your own diagnosis ("I think it's a race in
the repository") anchors the model onto your hypothesis and quietly turns an
investigation into a confirmation.

### Tier 4 — Long-running / multi-file (migrations, large features, sweeps)

Dominant risk: **drift**. Not misunderstanding — the model understood at turn 1
and lost the thread by turn 30. Context fills, early constraints fade, work
becomes locally sensible and globally inconsistent.
Add: everything from Tier 3, plus anchors that survive distance —

- An **invariant list**, phrased so it can be re-checked at any point ("every
  touched file still compiles"; "no public API changes").
- **Ordering**, when later steps depend on earlier ones being verified.
- **Checkpoints** — "after each module, run the tests and report before continuing."
- An explicit **out-of-scope** list, because drift is usually scope creep in slow motion.
- A **progress artifact** — a checklist file or task list the model updates, so the
  plan lives outside the context window instead of inside it.

> Migrate all 14 Room DAOs from callbacks to Flow, one module at a time.
> Order: `core/data`, then `feature/invoice`, then everything else.
> Invariants: no public API change outside the DAO layer; each module compiles
> and its tests pass before the next one starts.
> Out of scope: the Retrofit layer, and the pending schema v3 work.
> Keep a checklist and update it after each module. Stop and tell me if any DAO
> needs a signature change.

## The rewrite method

1. **Identify the tier** from the task's actual difficulty, not the prompt's
   current length. A one-line prompt can describe a Tier 4 job.
2. **Extract what's already there.** Users usually supply more than they realize —
   it's just unordered. Reordering beats inventing.
3. **Mark genuine gaps.** A gap is material only if two plausible readings lead to
   *different work*. Those need answers. Everything else gets a stated assumption,
   which is faster than asking and equally safe.
4. **Front-load the goal. Bottom-load the constraints.** Context in the middle.
5. **Cut.** Remove pleasantries, hedges, restatements, and any clause that fails
   the "what changes without this?" test. Cut role-play preambles ("You are an
   expert Android engineer") — they burn tokens and rarely change behavior on a
   concrete technical task, because the task itself already establishes the domain.
6. **Re-read as the model.** Ask literally: what would I do first? If the honest
   answer is "grep around to figure out what they mean," add the missing anchor.

## Output format

Keep it tight. Use this shape:

```
**Verdict:** <one line — the single biggest problem, or "already solid">

**Findings**
- [Blocking] <what's wrong> → <what it causes>
- [Costly] <what's wrong> → <what it causes>

**Rewritten**
<the rewritten prompt, ready to paste>

**Assumptions I baked in**
- <only if you made any>

**I need from you**
- <only genuinely material gaps — skip this section entirely if there are none>
```

Show the rewritten prompt as a paste-ready block, not as prose describing what to
change. The user's next action is copying it.

## Worked example

**Before:**
> can you look at the invoice thing and clean it up, its kind of a mess right now
> and i think theres some duplicate logic in there. also make sure you dont break
> anything

Findings: no concrete target (which "invoice thing" — module, file, screen?), no
definition of "clean up" (dedupe only, or restructure?), "don't break anything" is
unverifiable as written, and the real constraint — no behavior change — is implied
but never stated.

**After:**
> Remove the duplicated total-calculation logic in `feature/invoice/`.
> I believe it appears in both `InvoiceListViewModel` and `InvoiceDetailViewModel`;
> confirm before consolidating, and check whether other call sites exist.
> Behavior must not change — this is a pure refactor. Don't restructure the module
> or touch the Room entities.
> Done when the duplication is gone and `./gradlew :feature:invoice:test` passes.

Same intent, roughly the same length, but every sentence now removes a decision the
model would otherwise have to guess at. Note that "confirm before consolidating"
keeps the user's *uncertainty* intact instead of laundering their guess into a fact —
they said "I think", so the rewrite says "confirm."

## Anti-patterns

- **Inflation.** Turning a fine short prompt into a templated brief. If the tier is
  1 or 2, the rewrite should be comparably short.
- **Inventing requirements.** Adding constraints the user never expressed. If you
  think a constraint is likely wanted, put it under Assumptions where it's visible
  and correctable — not silently into the prompt body.
- **Laundering guesses into facts.** The user wrote "I think it's the cache." The
  rewrite must not say "the bug is in the cache."
- **Ceremony.** "You are a world-class engineer. Take a deep breath. Think step by
  step." On a concrete technical task with real file paths, this is dead weight.
- **Asking about non-material gaps.** Every question spends the user's attention.
  If a sensible default exists, state it as an assumption and move on.
