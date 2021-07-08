**DeltaEcore is a tool suite for model-based variability engineering in software product lines.** Its usage scenarios are manifold:

* Variability modeling via feature models or Hyper-Feature Models (HFMs; contain features versions) in the problem space of a software product line.
* Creation of custom delta languages for transformational variability engineering via delta modeling on Ecore-based languages/meta models.
* Transformational variability realization via delta modeling on language artifacts/models in the solution space of the software product line.

This document describes how to set up the tool suite from source code and, for that purpose, also briefly touches upon some of DeltaEcore's core concepts. To read more on the tool suite's concepts, visit [DeltaEcore's homepage at deltaecore.org](http://deltaecore.org/). If you want to cite this work in a paper, please head to the [Publications section](#publications). If you want to get in contact, please write an e-mail to **christoph** **[dot] seidl [at] deltaecore [dot] org**.

# General Concepts

Below is a brief overview of the general concepts of DeltaEcore with the sole purpose of making installation/usage of the tool suite easier to understand. For a more comprehensive explanation of essential concepts, visit [DeltaEcore's homepage at deltaecore.org](http://deltaecore.org/) or refer to one of the papers in the [Publications section](#publications).

## Architecture

DeltaEcore has the capacity for delta-language creation, variability modeling (problem space) and variability realization (solution space). Variability modeling in the problem space can, by default, be performed via feature models or Hyper-Feature Models (HFMs; contain features versions). Variability realization in the solution space can, by default, be performed via delta modeling using previously create delta languages custom tailored to a specific language, e.g., DeltaUML for UML models. The architecture of DetalEcore groups projects of the problem space and solution space as well as their connection, respectively, to create the following overall structure:

- **Core**: The back-end (i.e., handling the solution space) focusing on delta modeling, i.e., delta language generation, specifying variability via creating delta modules and deriving variants by applying delta modules. The respective Eclipse projects have the prefix *org.deltaecore.core*.
- **Feature**: The front-end (i.e., handling the problem space) focusing on creating feature models with constraints, defining configurations and triggering the variant derivation process. The respective Eclipse projects have the prefix *org.deltaecore.feature*.
- **Suite**: Gluing together the Core and Feature parts of the framework to create the tool suite DeltaEcore. The respective Eclipse projects have the prefix *org.deltaecore.suite*.

There is a clean separation between *Core* (delta-language creation/delta modeling/solution space) and *Feature* (variability modeling/problem space) part of DeltaEcore, i.e., you can use only the feature modeling part and plug in other variability realization mechanisms as well as use only the delta modeling part and plug in other variability modeling notations (or none).

## User Roles for DeltaEcore

DeltaEcore is (among others) an Eclipse-based tool suite for the generation of model-based delta languages. This circumstance leads to the fact that working with DeltaEcore means taking any one of three roles:

1. **Framework Developer**: Extends DeltaEcore with new functionality. Will modify the source code of the tool suite or build custom functionality on its basis.
2. **Delta Language Developer**: Directly uses DeltaEcore to create new delta languages for source languages, e.g., DeltaJava for Java. Will create delta dialects (*.decoredialect) to extend the common base delta language of DeltaEcore with suitable delta operations for a particular language’s meta model.
3. **Variability Engineer**: Indirectly uses DeltaEcore to apply delta languages to make language artifacts variable. Will create delta module (*.decore) files and use delta dialects by referencing them in delta modules.

## File Types

Due to its [many capacities](#architecture), DeltaEcore uses a variety of file types both to define delta languages and manage variability in a software product line. Below is a quick reference of the essential file types.

| File Type                     | Extension                       | Purpose                                                      | Notes                                                        |
| ----------------------------- | ------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **Delta Dialect**             | *.decoredialect                 | Definition of a delta language by providing the specifics of a custom delta language. | This file type is exclusively for the delta-language creation capacities of DeltaEcore whereas the others are related to variability modeling. |
| **Delta Module**              | *.decore                        | Use a previously defined delta language to modify artifacts in the course of variability engineering. |                                                              |
| Application-Order Constraints | *.deapplicationorderconstraints | Specify permissible (half) order in which delta modules may be applied, e.g., one delta module creates an element that another want wants to modify. | Only useful when delta modeling is used without a variability model. |
| **(Hyper) Feature Model**     | *.defeaturemodel                | Variability model that can either be a (common) feature model or a Hyper-Feature Model (HFM; contains feature versions). | Note that these files can be opened with **two editors** (right click -> Open With): The **Feature Model Editor** is meant to create/modify feature models; the **Feature Model Configurator** is meant to select configurations to derive products. |
| Feature Model Constraints     | *.deconstraints                 | Cross-tree constraints for a feature model, i.e., logical expressions over features (and versions) that further restrain configuration options. | By convention, the constraint file has to be located in the same folder as the feature model and have the same name, e.g., *Example.defeaturemodel* would go with *Example.deconstraints* in the same folder. |
| **Feature Mapping**           | *.demapping                     | Relates (combinations of) features to (sets of) delta modules so that a configuration from a feature model can be resolved to the delta modules realizing the respective changes. |                                                              |
| Configuration                 | *.deconfiguration               | A selection of features from a feature model (potentially with a specific version when using Hyper-Feature Models) that constitutes the conceptual representation of a valid product. |                                                              |

# Installation

## Installation from Source Code

Installation from source code is advised when aiming at extending the tool suite or getting a deeper understanding of its inner workings. The following sections explain the procedure of installation and some conceptual background.

### Prerequisites

Some preparatory steps have to be performed in order to install DeltaEcore from source code.

#### Install Eclipse

Install the current release of [Eclipse](https://www.eclipse.org/downloads/packages/) in either 32 bit or 64 bit (does not matter for DeltaEcore). As DeltaEcore builds upon Ecore, the easiest way is to get the “Eclipse Modeling Tools” release as it satisfies almost all modeling dependencies of DeltaEcore.

#### Install EMFText

Install EMFText into your Eclipse to create textual languages for Ecore meta models used by DeltaEcore.

~~EMFText Update Site:~~

- ~~Name: EMFText Update Site~~
- ~~URL: http://update.emftext.org/trunk/~~

*The EMFText Update Site has kept on causing problems for many people. As a remedy, there is a prepackaged version of EMFText delivered with the contents of the DeltaEcore repository. Briefly skip forward to [Clone the DeltaEcore Repository](#clone-the-deltaecore-repository), then check back here.*

In the DeltaEcore repository, within the *Dependencies* subfolder, there is a zip file for EMFText. Extract that file to the root of your Eclipse installation so that the contents of the contained *features* and *plugins* folders merge with their respective counterparts in Eclipse. Be sure to restart Eclipse before continuing the setup.

#### Set Up a Folder on Your Disk

Find a suitable location on your disk, where you can put all the files relating to DeltaEcore and, if you intend to do so, your future development based on the tool suite, e.g., "DeltaEcore". The remainder of this documentation will refer to this as the *root folder*. Ideally create a folder structure resembling the following:

- *Root Folder*
  - Repositories
    - DeltaEcore
    - EclipseTools
  - Workspaces
    - Development
    - Runtime
    - RuntimeRuntime

The folders within the *Workspaces* subfolder correlate with the different [user roles for DeltaEcore](#user-roles-for-deltaecore).

### Get the Source Code

#### Clone the Eclipse Tools Repository

The Eclipse Tools are a collection of utility functions to improve development with Eclipse and EMF Ecore. DeltaEcore uses these so that they have to be installed as a dependency.

Use Git to clone the [Eclipse Tools GitHub project](https://github.com/chseidl/eclipsetools) into your local folder *Repositories/EclipseTools*. Make sure that you clone directly into this folder so that, e.g., the retrieved folder *Code* is directly within the folder and not nested, e.g., as *eclipsetools/Code*.

#### Clone the DeltaEcore Repository  

Use Git to clone the [DeltaEcore GitHub project](https://github.com/chseidl/deltaecore) into your local folder *Repositories/DeltaEcore*. Make sure that you clone directly into this folder so that, e.g., the retrieved folder *Code* is directly within the folder and not nested, e.g., as *deltaecore/Code*.

### Set Up the Workspaces

The distinction into different [user roles for DeltaEcore](#user-roles-for-deltaecore) is essential due to the following reason:

Once deployed, neither of the roles needs to be aware of the creators logically before them as long as the DeltaEcore plugin with the relevant delta dialects is installed properly. However, when working with the source code directly, the relevant plugins are deployed dynamically to foster immediate testing of the newly modified implementations. This results in a structure of three workspaces for which the [folders were created earlier](#set-up-a-folder-on-your-disk).

1. Development: Workspace of the tool suite developer. When changes to the tool suite are to be tested dynamically, a runtime instance of Eclipse can be launched that leads to the Runtime workspace.
2. Runtime: Workspace of the delta language developer. New delta languages can be created here for languages/meta models that are registered in this instance (i.e., are defined in the development workspace). When the created delta languages are to be used dynamically, a new runtime instance has to be started leading to the RuntimeRuntime workspace.
3. RuntimeRuntime: Workspace of the variability engineer. Delta languages can be applied to create variable artifacts.

To keep a clean separation between the source code from the repositories and the metadata of Eclipse workspaces (as well as purely generated code), it is advisable to [use separate folders for repositories and workspaces as described earlier](#set-up-a-folder-on-your-disk).

#### Set Up the Development Workspace

To make Eclipse aware of the code from the cloned repositories, existing projects have to be imported into Eclipse. The DeltaEcore repository contains projects for either one of the [user roles for DeltaEcore](#user-roles-for-deltaecore) that have to be imported into the respective workspaces.

##### Import Development Projects

Open Eclipse in the workspace related to *Root Folder*/Workspaces/Development to import all projects relevant for the development workspace. **To import projects into your current Eclipse workspace, use File -> Import ... -> Existing Projects into Workspace**.

Note: For a better overview of the projects in the workspace, you can (ab)use Eclipse's workspace system by choosing **Top Level Elements -> Working Sets** from the pull down menu of the package explorer and creating a separate workspace for each category of projects, i.e., *Eclipse Tools*, *Framework*, and *Example Notations*.

###### Import the Eclipse Tools Projects

Import all projects from *Root Folder*/Repositories/EclipseTools/Code - ideally into a newly created working set *Eclipse Tools*. These are auxiliary projects used to extend Eclipse and deal with Ecore (meta) models.

###### Import the Framework Projects

Import all projects from *Root Folder*/Repositories/DeltaEcore/Code/Framework - ideally into a newly created working set *Framework*. These are the projects making up the core functionality of the DeltaEcore tool suite.

###### Import the Example Notations Projects

Import all projects from *Root Folder*/Repositories/DeltaEcore/Code/ExampleNotations - ideally into a newly created working set *Example Notations*. These are some example notations that are used as example subject systems to create delta languages (which is done from the Runtime instance pertaining to the delta language developer).

##### Generate Code

After successful completion of the project imports, all relevant projects for the development workspace of DeltaEcore should be present. However, they contain compile errors due to non-existent model code as this is not checked into source control and has to be recreated upon first install. The following explains the necessary steps.

###### Generate Code for Models

Many projects contain Ecore meta models that need source code to be generated. By convention, meta models and associated resources are located in a *models* subfolder of the project where applicable. To generate source code, the respective *.genmodel file has to be located within that folder and opened. Right click the root node of the Gen Model and select *Generate Model Code*.

The easiest way to catch all Gen Models is to use Eclipse's search function via *Search -> File...* and search for all files with a filename matching **.genmodel*. Gen Models have to be opened one by one after which their root element can be right clicked to select *Generate Model Code* from the context menu.

The model code has to be generated for the following projects:

Framework

- org.deltaecore.core.applicationorderconstraint
- org.deltaecore.core.decore
- org.deltaecore.core.decorebase
- org.deltaecore.core.decoredialect
- org.deltaecore.feature
- org.deltaecore.feature.configuration
- org.deltaecore.feature.configuration.migration
- org.deltaecore.feature.constraint
- org.deltaecore.feature.expression
- org.deltaecore.shellscript
- org.deltaecore.suite.mapping

Example Notations

- eu.vicci.eclipseproject
- eu.vicci.ecosystem.cl
- eu.vicci.ecosystem.componentfaultdiagram.cfd
- eu.vicci.ecosystem.goalstructuringnotation.gsn
- eu.vicci.ecosystem.sft
- org.emftext.language.docbook
- org.emftext.language.ecoreid
- org.emftext.language.umlid

In addition, auxiliary code has to be generated for the project org.deltaecore.feature by opening its Gen Model, right clicking its root element and selecting *Generate Edit Code*, then repeating the procedure and selecting *Generate Editor Code*.

Note: The order in which the projects are processed does not matter.

###### Generate Code for Languages

DeltaEcore uses multiple textual languages in the tool suite and as example notations. These textual languages were created using EMFText and need their respective source code generated. To generate source code for an EMFText language, locate the *.cs (concrete syntax) file in the model folder, right click it and select *Generate Text Resource* from the context menu.

The easiest way to catch all concrete syntax files is to use Eclipse's search function via *Search -> File...* and search for all files with a filename matching **.cs*. The code for a concrete syntax file can be generated directly from the search window by right clicking the respective file and selecting *Generate Text Resource* from the context menu.

The concrete syntax code has to be generated for the following projects:

Framework

- org.deltaecore.core.applicationorderconstraint
- org.deltaecore.core.decore
- org.deltaecore.core.decorebase
- org.deltaecore.core.decoredialect
- org.deltaecore.feature.configuration
- org.deltaecore.feature.constraint
- org.deltaecore.feature.expression
- org.deltaecore.shellscript
- org.deltaecore.suite.mapping

Example Notations

- eu.vicci.ecosystem.cl
- eu.vicci.ecosystem.componentfaultdiagram.cfd
- eu.vicci.ecosystem.sft
- org.emftext.language.docbook
- org.emftext.language.ecoreid
- org.emftext.language.umlid

Note: If there are build errors marked within the *.cs files, the most likely reason is that you installed the official EMFText release but not the most recent version from the update site (see above).

##### Fix Remaining Issues

The tool suite contains a few projects that serve a special purpose as they interface with or depend on other pieces of software. Most likely, you will not use these projects so that you should close them to not face errors resulting from unresolved dependencies (as you do not have the respective other pieces of software installed).

*If you do not have the Modeling Workflow Engine installed, close ...*

- org.deltaecore.installer

*If you do not have FeatureIDE installed, close ...*

- de.ovgu.featureide.deltaecore
- org.deltaecore.integration.ie.feature.eclipse
- org.deltaecore.integration.ie.feature.featureide

At this point, there should not be any compilation errors remaining in the development workspace.

#### Set Up the Runtime Workspace

The runtime workspace demonstrates how to use DeltaEcore to define new delta languages. For this purpose, the respective projects have to be imported. As a preparation, from the *Development* workspace, use Run -> Runtime Configurations ..., create a new Eclipse Application run configuration and be sure to **set its Location to ${workspace_loc}/../Runtime**, i.e., to use the previously defined folder *Root Folder*/Workspaces/Runtime.

##### Import the Example Delta Dialect Projects

Import all projects from *Root Folder*/Repositories/DeltaEcore/Code/Runtime. These are examples for creating delta languages for the example notations of the development workspace.

##### Generate Code for the Example Delta Languages

To generate code for the delta languages, you have to locate the *.decoredialect files, select each one, and, from the context menu, select Run As... -> decoredialect Application.

#### Set Up the RuntimeRuntime Workspace

The runtime runtime workspace demonstrates how to use DeltaEcore for variability modeling with the newly created delta languages as well as (hyper) feature models. For this purpose, the respective projects have to be imported. As a preparation, from the *Runtime* workspace, use Run -> Runtime Configurations ..., create a new Eclipse Application run configuration and be sure to **set its Location to ${workspace_loc}/../RuntimeRuntime**, i.e., to use the previously defined folder *Root Folder*/Workspaces/RuntimeRuntime.

##### Import the Example Usage Projects

Import all projects from *Root Folder*/Repositories/DeltaEcore/Code/RuntimeRuntime. These are examples for using the previously created delta languages as well as (Hyper) Feature Models for variability management.

As DeltaEcore can be used to define delta languages and manage variability where the latter can further be separated into modeling variability via (Hyper) Feature Models and realizing variability in artifacts via delta modules, the tool suite has many potential scenarios where combinations of its specific parts are used. The provided examples cover a part of the scenarios where combinations of delta language definition, variability modeling, and variability realization are used to various degrees. Here is a brief overview of what to find where:

| Project                    | Description                                                  | (Hyper) Feature Model                                | Delta Modules                                                | Notes                                                        |
| -------------------------- | ------------------------------------------------------------ | ---------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| DeltaEcore_RoSI_CROM       | A meta model family for role-based modeling and programming languages (with evolution history) | Hyper-Feature Model with many features and versions. | Many configuration and evolution delta modules.              | This is a comprehensive example capturing evolution. See sub-folder *documentation* for papers on the example's contents. |
| ... .cfd.delta.example     | Demonstration of delta-oriented variability on Component Fault Diagrams (CFDs) - a safety notation. | *None*                                               | Some configuration delta modules to demonstrate delta language. | This is a good starting point understand the capabilities of delta modules. |
| ... .gsn.delta.example     | Demonstration of delta-oriented variability on to goal structuring notation (GSN) - a safety notation. | *None*                                               | Just one configuration delta module to demonstrate delta language. | GSN has a graphical editor. This is a good starting point to understand variability. |
| ... .sft.delta.example     | Demonstration of delta-oriented variability on Software Fault Trees (SFTs) - a safety notation. | *None*                                               | Just two configuration delta modules to demonstrate delta language. | This is a good starting point understand the capabilities of delta modules and the integration with a textual language. |
| ExampleDeltaUsage          | Integration of delta languages for various languages in one project. | *None*                                               | Delta modules for three languages.                           |                                                              |
| HyVarMMHFM                 | Hyper-Feature Model of a car's ECU with multiple versions.   | Hyper-Feature Model with many features and versions. | *None*                                                       | Comprehensive example of a Hyper-Feature Model.              |
| ... .feature.delta.example | Bootstrapping of delta modules to target feature models.     | *None*                                               | Four evolution delta modules capturing change history to a Hyper-Feature Model. | This is *not* a regular use case but an experiment where a feature model notation is the subject of delta modeling. |
| ... .ecore.delta.example   | Bootstrapping of delta modules to target Ecore meta models (instead of models). | *None*                                               | Just one configuration delta module to demonstrate feasibility. | This is *not* a regular use case but an experiment where Ecore, i.e., the meta meta model, is the subject of delta modeling, i.e., one meta level higher than usually. |
| ... .uml.delta.example     | Demonstration of delta-oriented variability on UML models.   | *None*                                               | Multiple configuration delta modules.                        |                                                              |
| Snowflake                  | A language family of different feature modeling notations represented as software product line. | Feature model with many features.                    | Many configuration delta modules.                            | Similar to *Snowflake_Evolution* but *without* evolution history. |
| Snowflake_Evolution        | A language family of different feature modeling notations represented as software product line along with its evolution history. | Hyper-Feature Model with many features and versions. | Many configuration and evolution delta modules.              | Similar to *Snowflake* but *with* evolution history.         |
| TurtleBot                  | Software family for the Java-based configurable driver for the TurtleBot small domestic service robot. | Hyper-Feature Model with features and versions.      | Multiple configuration and evolution delta modules.          | Some implementation artifacts seem to have been lost over time. :( |

# Publications

If you want to cite DeltaEcore in a publication, please use the following reference:

```
@inproceedings{DBLP:conf/modellierung/SeidlSA14,
  author    = {Christoph Seidl and
               Ina Schaefer and
               Uwe A{\ss}mann},
  editor    = {Hans{-}Georg Fill and
               Dimitris Karagiannis and
               Ulrich Reimer},
  title     = {Delta{E}core - {A} Model-Based Delta Language Generation Framework},
  booktitle = {Modellierung 2014, 19.-21. M{\"{a}}rz 2014, Wien, {\"{O}}sterreich},
  series    = {{LNI}},
  volume    = {{P-225}},
  pages     = {81--96},
  publisher = {{GI}},
  year      = {2014},
  url       = {https://dl.gi.de/handle/20.500.12116/20956}
}
```

Below is a (partial) list of further publications pertaining DeltaEcore.

## Core Publications

- *DeltaEcore Framework*<br>
  C. Seidl, I. Schaefer, U. Aßmann<br>
  **DeltaEcore – A Model-Based Delta Language Generation Framework**<br>
  Proceedings of Modellierung’14, 2014<br>
  [Paper](http://subs.emis.de/LNI/Proceedings/Proceedings225/article2.html) | [Slides (*.pdf)](http://www.deltaecore.org/wp-content/uploads/2014_03_19-DeltaEcore-Christoph-Seidl-Modellierung14.pdf)
- *Hyper Feature Models (HFMs)*<br>
  C. Seidl, I. Schaefer, U. Aßmann<br>
  **Capturing Variability in Space and Time with Hyper Feature Models**<br>
  Proceedings of the 8th International Workshop on Variability Modelling of Software-Intensive Systems (VaMoS), 2014
- *Using Hyper Feature Models (HFMs) with DeltaEcore*<br>
  C. Seidl, I. Schaefer, U. Aßmann<br>
  **Integrated Management of Variability in Space and Time in Software Families**<br>
  Proceedings of the 18th International Software Product Line Conference (SPLC), 2014<br>
  [Paper](http://doi.acm.org/10.1145/2556624.2556625) | [Slides (*.pdf)](http://www.deltaecore.org/wp-content/uploads/2014_01_22-Capturing-Variability-in-Space-and-Time-with-Hyper-Feature-Models-Christoph-Seidl-VaMoS14.pdf)

## Extended Publications

- *DeltaEcore as Influence on a Unified Model for Managing Variability in Space and Time*
  S. Ananieva, S. Greiner, T. Kühn, J. Krüger, L. Linsbauer, S. Grüner, T. Kehrer, H. Klare, A. Koziolek, H. Lönn, S. Krieter, C. Seidl, S. Ramesh, R. Reussner, B. Westfechtel
  **A Conceptual Model for Unifying Variability in Space and Time**
  Proceedings of the 24th International Systems and Software Product Line Conference (SPLC), 2020
- *Book Chapter on DeltaEcore's Capacities in Context of Software Product Line Construction*
  C. Pietsch, C. Seidl, M. Nieke, T. Kehrer
  **Delta-Oriented Development of Model-Based Software Product Lines with DeltaEcore and SiPL: A Comparison**
  in Model Management and Analytics for Large Scale Systems, 2019
- *Book Chapter on DeltaEcore's Capacities in Context of Software Product Line Reverse Engineering*
  C. Seidl, D.Wille, I. Schaefer
  **Software Reuse – From Cloned Variants to Managed Software Product Lines**
  in Automotive Software Engineering – M. v. d. Brand, Y. Dajsuren (Eds.), 2018
- *DeltaEcore as Technological Backbone of the EU Project HyVar*
  T. B. Røst, C. Seidl, I. C. Yu, F. Damiani, E. B. Johnsen, C. Chesta
  **HyVar – Scalable Hybrid Variability for Distributed Evolving Software Systems**
  Proceedings of European Conference on Service-Oriented and Cloud Computing (ESOCC) Workshops, 2017
- *Reverse Engineer Product Portfolio to Software Product Line and Generate DeltaEcore Languages/Delta Modules*
  D. Wille, T. Runge, C. Seidl, S. Schulze
  **Extractive Software Product Line Engineering using Model-Based Delta Module Generation**
  Proceedings of the 11th International Workshop on Variability Modelling of Software-Intensive Systems (VaMoS), 2017
- *DeltaEcore as Basis for Tool DarwinSPL*
  M. Nieke, G. Engel, C. Seidl
  **DarwinSPL: An Integrated Tool Suite for Modeling Evolving Context-Aware Software Product Lines**
  Proceedings of the 11th International Workshop on Variability Modelling of Software-Intensive Systems (VaMoS), 2017
- *Delta Language for Ecore Meta Models and Case Study on Family of Feature-Modeling Notations*
  C. Seidl, T. Winkelmann, I. Schaefer
  **A Software Product Line of Feature Modeling Notations and Cross-Tree Constraint Languages**
  Proceedings of Modellierung'16, 2016
- *Delta Language for Ecore Meta Models and Case Study on Family of Role-Based Modeling and Programming Languages*
  T. Kühn, M. Leuthäuser, S. Götz, C. Seidl, U. Aßmann
  **A Metamodel Family for Role-Based Modeling and Programming Languages**
  Proceedings of the 7th International Conference on Software Language Engineering (SLE), 2014
- *Delta Language and Analyses for Component Fault Diagrams (CFDs) - A Safety Notation*
  C. Seidl, I. Schaefer, U. Aßmann
  **Variability-Aware Safety Analysis using Delta Component Fault Diagrams**
  Proceedings of the 4th International Workshop on Formal Methods and Analysis in Software Product Line Engineering (FMSPLE), 2013