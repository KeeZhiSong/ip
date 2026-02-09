# Git Commit Guidelines

Follow these rules for all future commits to maintain consistency and quality.

## Subject Line Rules

### ✅ DO:
- **Use imperative mood**: "Add feature" not "Added feature" or "Adding feature"
- **Capitalize first letter**: "Move file" not "move file"
- **Keep it under 50 characters** (hard limit: 72)
- **No period at end**: "Update README" not "Update README."
- **Add scope when helpful**: "GUI: Fix button alignment" or "Parser: Handle edge case"

### ❌ DON'T:
- Use past tense ("Added", "Fixed")
- Use present continuous ("Adding", "Fixing")
- Exceed 72 characters
- End with period

### Examples:
```
✅ Good:
- Add find command to search tasks
- Fix mark command argument parsing
- GUI: Improve dialog text wrapping
- Parser: Handle empty input gracefully
- chore: Update dependencies

❌ Bad:
- Added find command to search tasks (past tense)
- Fixed the mark command argument parsing issue. (period at end)
- The GUI dialog text wrapping has been improved (not imperative, too long)
```

## Commit Body Rules

### When to add a body:
- **Always** for non-trivial commits
- When the change requires explanation
- When fixing a bug or issue
- When making design decisions

### Structure:
```
{Subject line - imperative, <50 chars}

{Current situation - present tense}

{Why it needs to change}

{What is being done - use "Let's:" to introduce}

{Why it's done this way}

{Any other relevant info}
```

### Formatting:
- Separate subject from body with **blank line**
- Wrap body at **72 characters**
- Use **bullet points** when listing changes
- Use blank lines to separate paragraphs

### Example:
```
Parser: Fix argument extraction for GUI commands

The GUI handlers were passing full input strings to parseTaskIndex().

parseTaskIndex() expects only numeric arguments, causing parseInt()
to fail when receiving strings like 'mark 1' instead of just '1'.

Let's:
* Update handleMarkForGui to pass arguments instead of input
* Update handleUnmarkForGui to pass arguments instead of input
* Update handleDeleteForGui to pass arguments instead of input

This ensures all GUI commands parse numeric arguments correctly
without requiring code changes to the parser itself.
```

## Quick Checklist

Before committing, verify:
- [ ] Subject uses imperative mood ("Add" not "Added")
- [ ] Subject is capitalized
- [ ] Subject is under 50 chars (max 72)
- [ ] No period at end of subject
- [ ] Blank line between subject and body
- [ ] Body explains WHAT and WHY (not HOW)
- [ ] Body is wrapped at 72 characters
- [ ] Bullet points used where appropriate

## Common Scopes

Use these scope prefixes when applicable:
- `GUI:` - User interface changes
- `Parser:` - Input parsing logic
- `Storage:` - File I/O and persistence
- `Task:` - Task-related classes
- `UI:` - Terminal UI components
- `docs:` - Documentation only
- `chore:` - Build, dependencies, tooling
- `test:` - Test additions or changes
- `fix:` - Bug fixes
- `feat:` - New features

## Resources

- [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
- [SE-EDU Git Conventions](https://se-education.org/guides/conventions/git.html)
- [Conventional Commits](https://www.conventionalcommits.org/)
