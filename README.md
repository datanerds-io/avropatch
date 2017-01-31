[![Build Status](https://travis-ci.org/datanerds-io/avropatch.svg?branch=develop)](https://travis-ci.org/datanerds-io/avropatch)
[![Code Coverage](https://codecov.io/github/datanerds-io/avropatch/coverage.svg)](https://codecov.io/gh/datanerds-io/avropatch/branch/develop)

# avropatch
Avro implementation of a PATCH payload as defined for JSON in RFC 6902.

Avropatch is a lightweight implementation of a PATCH change set which is serializable by Avro and compatible to the JSON objects defined by RFC 6902. Since it consists of a sequence of operations expressing partial updates to an object it may be used to avoid sending the whole object when only a part of it has changed.

