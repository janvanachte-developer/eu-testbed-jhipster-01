import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MetaData } from './meta-data.model';
import { MetaDataPopupService } from './meta-data-popup.service';
import { MetaDataService } from './meta-data.service';

@Component({
    selector: 'jhi-meta-data-delete-dialog',
    templateUrl: './meta-data-delete-dialog.component.html'
})
export class MetaDataDeleteDialogComponent {

    metaData: MetaData;

    constructor(
        private metaDataService: MetaDataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.metaDataService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'metaDataListModification',
                content: 'Deleted an metaData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-meta-data-delete-popup',
    template: ''
})
export class MetaDataDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metaDataPopupService: MetaDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.metaDataPopupService
                .open(MetaDataDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
