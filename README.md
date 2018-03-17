# treff. - An Instant Messenger focused on live location transmission

## Main features
* Live GPS-based location transmission to groups of users.
Locations are only transmitted when users explicitly activate the feature and
will automatically turn off after a set time.
* OpenStreetMaps view showing all currently known locations of contacts
* Completely open-source, no proprietary dependencies (No Google Play
  Services!)
* Self-hosted server

## Components of this repository
* `client`: Android client (Android Studio project)
* `server`: Java servlet implementation of the server API (IntelliJ IDEA
  project).
Note that the commands `delete-account`, `reset-password`, and
`reset-password-confirm` and description checksums are not implemented yet.
The server also doesn't enforce transport encryption.
Server admins are advised to disallow connections via insecure channels.
* `implementierung/protocol`: Server API
* `pflichtenheft`: Functional specification and presentation thereof (German
  document)
* `entwurf`: Design document and presentation thereof (German document, figures
  in English)
* `implementierung`: Documentation of design changes made during implementation
and presentation thereof (German document)
* `test`: Quality assurance documents and presentation thereof (German
  document)
* `abschluss`: Final presentation (German document)

Note that the documents in `pflichtenheft`, `entwurf`, `test`, `abschluss`, and
`implementation` (except `protocol`) might contain outdated documents as those
were created at different points in time during development.

## Development status
This is a student project that started as part of a software development module
at the Karlsruhe Institute of Technology (KIT).
Some features might be missing or broken and we might not find the time or
motivation to actively implement/fix them in the near future.

## Contributing
We haven't really thought about how we're gonna handle this in detail but
feel free to report issues and create pull requests.
