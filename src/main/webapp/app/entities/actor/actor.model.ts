import { BaseEntity } from './../../shared';

export class Actor implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public endpoint?: string,
        public testSuiteId?: number,
    ) {
    }
}
