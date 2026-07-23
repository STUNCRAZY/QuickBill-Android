# QuickBill-Android

**The shop counter, not an app.**

QuickBill is an offline-first Android invoicing tool for tradesmen that replaces abstract SaaS screens with physical objects they already know:

- Order Pad → create invoice
- Ticket Rail → unpaid jobs
- Cash Drawer → paid history
- Rolodex → customers
- Supplier Catalog → materials by project (Framing, Gazebo, Pergola, HVAC…)
- Corkboard → follow-ups & messages

**Design rules (non-negotiable)**
- Full-bleed 9:16 physical surfaces only
- Zero Material Design chrome, zero bottom nav, zero FABs, zero sidebars
- Paper, steel, wood textures + cool-blue accents + amber for overdue
- Gestures that feel real (flick-to-tear, long-press voice, page flip)

## Tech Stack
- Kotlin
- Jetpack Compose (100% UI)
- Clean Architecture + MVVM/MVI
- Room (local-first)
- Coil, WorkManager, SpeechRecognizer, PdfDocument

## Modules
- `:app` – single activity, immersive
- `:core:ui` – theme, Paper, Ticket, Pad, Drawer components
- `:core:domain` – entities & use cases
- `:core:data` – Room + repositories
- `:feature:counter` – Home / Shop Counter
- `:feature:invoice` – Invoice + Quote + Carbon
- `:feature:catalog` – Projects + Materials
- `:feature:rolodex` – Customers
- `:feature:tickets` – Ticket detail + rail logic
- `:feature:cashdrawer`
- `:feature:corkboard`

## Getting Started
Open in Android Studio (Ladybug or newer), sync Gradle, run on a physical device or emulator with gesture navigation for the best full-bleed experience.

Built with the philosophy: *if a tradesman has to think about how to use it, we failed.*
