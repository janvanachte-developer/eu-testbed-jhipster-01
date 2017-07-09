import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { MetaData } from './meta-data.model';
import { MetaDataService } from './meta-data.service';

@Component({
    selector: 'jhi-meta-data-detail',
    templateUrl: './meta-data-detail.component.html'
})
export class MetaDataDetailComponent implements OnInit, OnDestroy {

    metaData: MetaData;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private metaDataService: MetaDataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMetaData();
    }

    load(id) {
        this.metaDataService.find(id).subscribe((metaData) => {
            this.metaData = metaData;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMetaData() {
        this.eventSubscriber = this.eventManager.subscribe(
            'metaDataListModification',
            (response) => this.load(this.metaData.id)
        );
    }
}
