import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Specification } from './specification.model';
import { SpecificationPopupService } from './specification-popup.service';
import { SpecificationService } from './specification.service';

@Component({
    selector: 'jhi-specification-delete-dialog',
    templateUrl: './specification-delete-dialog.component.html'
})
export class SpecificationDeleteDialogComponent {

    specification: Specification;

    constructor(
        private specificationService: SpecificationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.specificationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'specificationListModification',
                content: 'Deleted an specification'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-specification-delete-popup',
    template: ''
})
export class SpecificationDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private specificationPopupService: SpecificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.specificationPopupService
                .open(SpecificationDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
