

# SHAVADOOP (TELECOM PARISTECH Project)

> This project aims to implement a distributed version of Wordcount in JAVA

## Project Description

This project involves implementing a version of ‘Wordcount’, a program that counts words in a document in the Hadoop distributed file system.

This type of distributed program is very important when the size of the data is large and especially in this specific case when the input file is quite large.

In order to perform distributed processing, our program will use computing resources (memory, processor, etc.) from several machines to launch processing in parallel.

For the implementation of our solution, we created two projects:

- MASTER_SHAVADOOP: master program responsible for performing minor treatments and mainly for coordinating all treatments. It can be seen as the Namenode in the distributed file management system in Hadoop.

- SLAVE_SHAVADOOP: program responsible for carrying out major processing (writing and reading of data, etc.). It can be seen as the Datanode in the distributed file management system in Hadoop.

For more, please refer to:

[SHAVADOOP_Rapport.docx](https://github.com/ericfokou/SHAVADOOP/blob/master/SHAVADOOP_Rapport.docx)

## Requirement

-  JDK

## Release History

* 0.0.dev0
    * First development  release 

## Questions?

If you find a bug, feel free to write me [fokoub@gmail.com](mailto:fokoub@gmail.com).

## Contributing

Fork it:

	$ git clone https://github.com/ericfokou/SHAVADOOP

Then create your feature branch and commit your changes.

## Support

Eric FOKOU 

- email: [fokoub@gmail.com](mailto:fokoub@gmail.com)
- Twitter: <a href="http://twitter.com/fokou_eric" target="_blank">`@fokou_eric`</a>



