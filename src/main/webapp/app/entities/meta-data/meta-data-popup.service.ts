import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { MetaData } from './meta-data.model';
import { MetaDataService } from './meta-data.service';

@Injectable()
export class MetaDataPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private metaDataService: MetaDataService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.metaDataService.find(id).subscribe((metaData) => {
                metaData.published = this.datePipe
                    .transform(metaData.published, 'yyyy-MM-ddThh:mm');
                metaData.lastModified = this.datePipe
                    .transform(metaData.lastModified, 'yyyy-MM-ddThh:mm');
                this.metaDataModalRef(component, metaData);
            });
        } else {
            return this.metaDataModalRef(component, new MetaData());
        }
    }

    metaDataModalRef(component: Component, metaData: MetaData): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.metaData = metaData;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
