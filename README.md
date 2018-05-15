# crdt-editor
The application is a collaborative, real-time text editor that relies on IP multicast to communicate between co-editors.

This JavaFx application is written in Java as a Gradle (dependency manager similar to Maven) project. Therefore, at least JDK 1.8 is required. 

## Development
As this is a Gradle project, it can be easily imported as such in the IDE of your choice.
The Gradle configuration supports multiple tasks and wraps a self-contained Gradle installation meaning a system installation of Gradle is not required. This means, you can run any Gradle task in your project using the gradlew shell script or bat file located in the root directory.
However a Gradle plugin for your IDE will be needed to execute the Gradle tasks from within the IDE.

From within the project's root directory, you can execute any Gradle task by running

 - `./gradlew <task>` (on Unix-like platforms such as Linux and Mac OS X)
 - `gradlew <task>` (on Windows using the gradlew.bat batch file)

where \<task\> is replaced with test/build/installDist/clean etc.
Arguments have to be changed accordingly, if necessary.

## Usage
After the project has been installed the project's root directory has a build/install/bin subfolder that contains ready-to-use start scripts for both Windows and Unix systems.
To execute the application on e.g. Windows run the following command in the aforementioned subfolder:

`COMP90020-CRDT.bat`

By default, communication occurs via an IP multicast group listening to 224.0.0.15 on port 9999.

## Architecture
You will find three main packages crdt, messenger, and text editor in the code base that encapsulate the corresponding functionalities and are separated by generic interfaces so that it is easy to extend and/or replace parts of them.

The crdt package contains the implementation of Treedoc as outlined in our report. At the core, it is a binary tree similar to ones that are used for Huffman encoding. Its nodes contain mininodes which itself encapsulate a unique position, the character atom and whether it has been deleted or not. The binary tree is facilitated to produce new unique position id on character insertion and marking atoms as tombstones upon deletion.

The network package contains the various network-related tasks involved in the communication with co-editors of a document. It's main interface to other packages is placed in the message subpackage.
A group can be joined(), left(), send(Message) to and a callback can be set that is called whenever a new message is received. These interfaces are implemented for CRDT purposes but are fundamentally generic. A group can be created through the Network singleton class.

Two groups have been implemented in the respective subpackages multicast and ordering: an IP mutlicast group and an ordered group that transparently applies dynamic (vector) timestamp ordering on messages sent to and received from this group. The ordered group internally uses a multicast group.``

The main texteditor package contains classes necessary to bootstrap the application and update the UI.


## Comments
This project is a prototype and contains a few major restrictions such as lack of persistence, no group membership updates and no cursor position updates.
However, the prototype succeeds in showcasing the strengths and suitability of CRDTs in collaborative, real-time editing.

At last, have fun diving in the code!