/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { APGroupMySuffixComponent } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.component';
import { APGroupMySuffixService } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.service';
import { APGroupMySuffix } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.model';

describe('Component Tests', () => {

    describe('APGroupMySuffix Management Component', () => {
        let comp: APGroupMySuffixComponent;
        let fixture: ComponentFixture<APGroupMySuffixComponent>;
        let service: APGroupMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APGroupMySuffixComponent],
                providers: [
                    APGroupMySuffixService
                ]
            })
            .overrideTemplate(APGroupMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APGroupMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APGroupMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new APGroupMySuffix(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.aPGroups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
