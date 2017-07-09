import { BaseEntity } from './../../shared';

export class MetaData implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public version?: string,
        public authors?: string,
        public description?: string,
        public published?: any,
        public lastModified?: any,
    ) {
    }
}
