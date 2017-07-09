import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Specification } from './specification.model';
import { SpecificationService } from './specification.service';

@Component({
    selector: 'jhi-specification-detail',
    templateUrl: './specification-detail.component.html'
})
export class SpecificationDetailComponent implements OnInit, OnDestroy {

    specification: Specification;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private specificationService: SpecificationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSpecifications();
    }

    load(id) {
        this.specificationService.find(id).subscribe((specification) => {
            this.specification = specification;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSpecifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'specificationListModification',
            (response) => this.load(this.specification.id)
        );
    }
}
